package subsistence.common.tile.machine;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.block.machine.BarrelType;
import subsistence.common.block.machine.CompostType;
import subsistence.common.config.CoreSettings;
import subsistence.common.config.HeatSettings;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.BarrelMeltingRecipe;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import subsistence.common.tile.core.TileCoreMachine;
import subsistence.common.util.ArrayHelper;
import subsistence.common.util.InventoryHelper;

public final class TileBarrel extends TileCoreMachine {

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
        if (worldObj.isRemote)
            return;

        collectRainWater();

        if (isWood()) {
            if (fluidContents != null && fluidContents.getFluid() == FluidRegistry.LAVA) {
                if (fluidContents.amount >= 1000) {
                    worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.flowing_lava);
                } else {
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                }
            }
            processWoodRecipe();
        } else {
            processWoodRecipe();
            processStoneRecipe();
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
        return getType() == BarrelType.WOOD;
    }

    /* GENERAL HELPerS */
    public BarrelType getType() {
        return ArrayHelper.safeGetArrayIndex(BarrelType.values(), getBlockMetadata());
    }

    public boolean addFluid(FluidStack fluidStack) {
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
            itemContents = new ItemStack[getType().itemCapacity];
            fluidContents = null;

            if (cachedWoodRecipe.outputItem != null)
                itemContents = new ItemStack[] {cachedWoodRecipe.outputItem.copy()};

            if (cachedWoodRecipe.outputLiquid != null)
                fluidContents = cachedWoodRecipe.outputLiquid.copy();

            reset();

            markForUpdate();
        }
    }

    private void collectRainWater() {
        if (cachedWoodRecipe != null)
            return;

        if (!worldObj.isRaining() || !worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord))
            return;

        if (rainDelay == -1) {
            rainDelay = RANDOM.nextInt(500);
        } else {
            if (rainDelayTick >= rainDelay) {
                rainDelayTick = 0;
                rainDelay = RANDOM.nextInt(500);

                if (!hasLid) {
                    addFluid(new FluidStack(FluidRegistry.WATER, CoreSettings.STATIC.barrelRain));
                }
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
                    itemContents = new ItemStack[getType().itemCapacity];
                    fluidContents = null;

                    if (cachedStoneRecipe.outputItem != null)
                        itemContents = new ItemStack[] {cachedStoneRecipe.outputItem.copy()};

                    if (cachedStoneRecipe.outputLiquid != null)
                        fluidContents = cachedStoneRecipe.outputLiquid.copy();

                    reset();

                    markForUpdate();
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

    private ItemStack getFirstItem() {
        for (int i=0; i<itemContents.length; i++) {
            ItemStack itemStack = itemContents[i];
            if (itemStack != null)
                return itemStack;
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
}