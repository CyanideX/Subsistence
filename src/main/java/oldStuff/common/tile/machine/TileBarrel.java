package oldStuff.common.tile.machine;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import oldStuff.SubsistenceOld;
import oldStuff.common.config.CoreSettings;
import oldStuff.common.config.HeatSettings;
import oldStuff.common.item.SubsistenceItems;
import oldStuff.common.lib.MachineType;
import oldStuff.common.network.nbt.NBTHandler;
import oldStuff.common.recipe.SubsistenceRecipes;
import oldStuff.common.recipe.wrapper.BarrelMeltingRecipe;
import oldStuff.common.recipe.wrapper.BarrelStoneRecipe;
import oldStuff.common.recipe.wrapper.BarrelWoodRecipe;
import oldStuff.common.tile.core.TileCoreMachine;
import oldStuff.common.util.ArrayHelper;
import oldStuff.common.util.InventoryHelper;

public final class TileBarrel extends TileCoreMachine implements ISidedInventory {

    public static final float DIMENSION_FILL = 0.8F - 0.0625F;

    /* GENERAL */
    @NBTHandler.Sync(true)
    public ItemStack[] itemContents;
    @NBTHandler.Sync(true)
    public FluidStack fluidContents;

    @NBTHandler.Sync(true)
    public boolean hasLid;

    /* WOOD SPECIFIC */
    private int rainDelayTick = 0;
    private int rainDelay = -1;

    private BarrelWoodRecipe cachedWoodRecipe;

    /* STONE SPECIFIC */
    @NBTHandler.Sync(true)
    private int processingTime;
    @NBTHandler.Sync(true)
    private int maxProcessingTime;

    private BarrelStoneRecipe cachedStoneRecipe;
    private BarrelMeltingRecipe cachedMeltingRecipe;

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            collectRainWater();

            if (isWood()) {
                if (fluidContents != null && fluidContents.getFluid() == FluidRegistry.LAVA) { // wood barrel cant have lava!
                    if (fluidContents.amount >= 1000) {
                        worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.flowing_lava);
                        return;
                    } else {
                        worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                        return;
                    }
                }
                processWoodRecipe();
            } else {
                processWoodRecipe();
                processStoneRecipe();
            }
        }
    }

    @Override
    public void onBlockBroken() {
        if (itemContents != null) {
            for (ItemStack itemStack : itemContents) {
                InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, itemStack, RANDOM);
            }
        }
        if (hasLid) {
            InventoryHelper.dropItem(worldObj, xCoord, yCoord, zCoord, ForgeDirection.UNKNOWN, new ItemStack(SubsistenceItems.barrelLid, 1, getBlockMetadata()), RANDOM);
        }
    }

    /* STATE */
    private boolean isWood() {
        return getType() == MachineType.BarrelType.WOOD;
    }

    /* GENERAL HELPERS */
    public MachineType.BarrelType getType() {
        return ArrayHelper.safeGetArrayIndex(MachineType.BarrelType.values(), getBlockMetadata());
    }

    public boolean addFluid(FluidStack fluidStack, boolean dumpExcess) {
        if (fluidContents == null || fluidContents.getFluid() == null) {
            fluidContents = fluidStack.copy();

            reset();
            markForUpdate();
            return true;
        } else {
            if (fluidContents.getFluid() == fluidStack.getFluid()) {
                if (fluidContents.amount + fluidStack.amount <= getType().fluidCapacity) {
                    fluidContents.amount += fluidStack.amount;

                    reset();
                    markForUpdate();
                    return true;
                } else if (dumpExcess) {
                    fluidContents.amount = getType().fluidCapacity;
                    reset();
                    markForUpdate();
                    return true;
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public boolean addItem(ItemStack itemStack) {
        if (itemContents == null || itemContents.length != getType().itemCapacity) {
            itemContents = new ItemStack[getType().itemCapacity];
        }

        for (int i=0; i<itemContents.length; i++) {
            ItemStack contents = itemContents[i];

            if (contents == null) {
                itemContents[i] = itemStack.copy();

                reset();
                markForUpdate();
                return true;
            }
        }
        return false;
    }

    private void reset() {
        cachedWoodRecipe = null;
        cachedStoneRecipe = null;
        cachedMeltingRecipe = null;
        processingTime = 0;
        maxProcessingTime = 0;
    }

    /* WOOD SPECIFIC */
    private void processWoodRecipe() {
        if (cachedWoodRecipe == null) {
            cachedWoodRecipe = SubsistenceRecipes.BARREL.getWooden(itemContents, fluidContents);
        } else {
            // Wood recipes are immediate
            if (cachedWoodRecipe.valid(itemContents,fluidContents)) { //is current recipe valid?
                itemContents = new ItemStack[getType().itemCapacity];
                fluidContents = null;

                if (cachedWoodRecipe.outputItem != null)
                    itemContents[0] = cachedWoodRecipe.outputItem.copy();
                if (cachedWoodRecipe.outputLiquid != null)
                    fluidContents = cachedWoodRecipe.outputLiquid.copy();

                reset();
                markForUpdate();
            }
        }
    }

    private void collectRainWater() {
        if (cachedWoodRecipe != null)
            return;

        if (!worldObj.isRaining() || !worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord))
            return;

        if (rainDelay == -1) {
            rainDelay = RANDOM.nextInt(500);
        } else {
            if (rainDelayTick >= rainDelay) {
                rainDelayTick = 0;
                rainDelay = RANDOM.nextInt(500);

                if (!hasLid) {
                    addFluid(new FluidStack(FluidRegistry.WATER, CoreSettings.barrelRain),true);
                }
                reset();
            } else {
                rainDelayTick++;
            }
        }
    }

    /* STONE SPECIFIC */
    private void processStoneRecipe() {
        if (!hasHeatSource())
            return;

        // Check for proper stone recipes first
        if (cachedStoneRecipe == null) {
            cachedStoneRecipe = SubsistenceRecipes.BARREL.getStone(itemContents, fluidContents);
        } else {
            if (maxProcessingTime <= 0) {
                maxProcessingTime = getProcessingTime(false);
            } else {
                if (processingTime < maxProcessingTime) {
                    processingTime++;
                } else {
                    if (cachedStoneRecipe.valid(itemContents,fluidContents)) {
                        itemContents = new ItemStack[getType().itemCapacity];
                        fluidContents = null;

                        if (cachedStoneRecipe.outputItem != null)
                            itemContents[0] = cachedWoodRecipe.outputItem.copy();
                        if (cachedStoneRecipe.outputLiquid != null)
                            fluidContents = cachedStoneRecipe.outputLiquid.copy();

                        reset();
                        markForUpdate();
                    }
                }
            }
        }

        // Then melting recipes
        if (cachedMeltingRecipe == null) {
            cachedMeltingRecipe = SubsistenceRecipes.BARREL.getMelting(getFirstItem());
        } else {
            if (fluidContents != null)
                if (fluidContents.amount + cachedMeltingRecipe.output.amount > getType().fluidCapacity)
                    return;

            if (maxProcessingTime <= 0) {
                maxProcessingTime = getProcessingTime(true);
            } else {
                if (processingTime < maxProcessingTime) {
                    processingTime++;
                } else {
                    if (cachedMeltingRecipe.valid(getFirstItem())) {
                        removeFirstItem();

                        FluidStack output = cachedMeltingRecipe.output;
                        if (fluidContents == null) {
                            fluidContents = output.copy();
                        } else {
                            fluidContents.amount += output.amount;
                        }
                        reset();
                        markForUpdate();
                    }
                }
            }
        }
    }

    private ItemStack getFirstItem() {
        if (itemContents != null && itemContents.length > 0) {
            for (int i = 0; i < itemContents.length; i++) {
                ItemStack itemStack = itemContents[i];
                if (itemStack != null) {
                    return itemStack;
                }
            }
        }
        return null;
    }

    private void removeFirstItem() {
        for (int i=0; i<itemContents.length; i++) {
            if (itemContents[i] != null) {
                itemContents[i] = null;
                return;
            }
        }
    }

    private boolean hasHeatSource() {
        return HeatSettings.isHeatSource(worldObj, xCoord, yCoord - 1, zCoord);
    }

    private int getProcessingTime(boolean melting) {
        if (melting) {
            if (HeatSettings.isTorch(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedMeltingRecipe.timeTorch;
            } else if (HeatSettings.isLava(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedMeltingRecipe.timeFire;
            } else if (HeatSettings.isFire(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedMeltingRecipe.timeLava;
            } else {
                return -1;
            }
        } else {
            if (HeatSettings.isTorch(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedStoneRecipe.timeTorch;
            } else if (HeatSettings.isLava(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedStoneRecipe.timeFire;
            } else if (HeatSettings.isFire(worldObj, xCoord, yCoord - 1, zCoord)) {
                return cachedStoneRecipe.timeLava;
            } else {
                return -1;
            }
        }
    }

    /* ISidedInventory */
    @Override
    public int getSizeInventory() {
        if (itemContents == null) {
            itemContents = new ItemStack[getType().itemCapacity];
        }
        return itemContents.length;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        return itemContents[p_70301_1_ % itemContents.length];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amnt) {
        if (this.itemContents[slot] != null) {
            ItemStack itemstack;

            if (this.itemContents[slot].stackSize <= amnt) {
                itemstack = this.itemContents[slot];
                this.itemContents[slot] = null;
                this.markDirty();
                this.reset();
                this.markForUpdate();
                return itemstack;
            } else {
                itemstack = this.itemContents[slot].splitStack(amnt);

                if (this.itemContents[slot].stackSize == 0) {
                    this.itemContents[slot] = null;
                }
                
                this.markDirty();
                this.reset();
                this.markForUpdate();
                return itemstack;
            }
            
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.itemContents[slot] != null) {
            ItemStack itemstack = this.itemContents[slot];
            this.itemContents[slot] = null;
            this.reset();
            this.markForUpdate();
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.itemContents[slot] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.reset();
        this.markDirty();
        this.markForUpdate();
    }

    @Override
    public String getInventoryName() {
        return SubsistenceOld.RESOURCE_PREFIX + ".barrel";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this 
                && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot < getType().itemCapacity;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (itemContents == null) {
            itemContents = new ItemStack[getType().itemCapacity];
        }
        
        if (side > 2) {
            return new int[0];
        } else {
            int[] slots = new int[itemContents.length];
            for (int i = 0; i < slots.length; i++) {
                slots[i] = i;
            }
            return slots;
        }
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return side == 1 && ArrayUtils.contains(getAccessibleSlotsFromSide(side), slot);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side) {
        return side == 0 && ArrayUtils.contains(getAccessibleSlotsFromSide(side), slot);
    }
}