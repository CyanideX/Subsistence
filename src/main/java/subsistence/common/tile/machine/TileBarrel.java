package subsistence.common.tile.machine;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.config.MainSettingsStatic;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.network.packet.PacketHelper;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import subsistence.common.tile.core.TileCoreMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class TileBarrel extends TileCoreMachine {

    private static final Random RANDOM = new Random();

    public static final float DIMENSION_FILL = 0.8F - 0.0625F;

    public final float maxTemperature = 32;

    public boolean needHeat = false;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public FluidStack fluid;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public ItemStack[] contents;

    @NBTHandler.NBTData
    public int processTimeElapsed = 0;

    @NBTHandler.NBTData
    public float currentTemperature = 0F;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    private boolean hasLid;

    private int delayTick = 0;
    private int randomDelay = -1;

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public boolean hasLid() {
        return this.hasLid;
    }

    public void toggleLid() {
        this.hasLid = !this.hasLid;
        this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote)
            return;

        if (worldObj.isRaining() && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord)) {
            if (randomDelay == -1) {
                randomDelay = RANDOM.nextInt(500); // 0 to 25 seconds
            } else {
                if (delayTick >= randomDelay) {
                    randomDelay = RANDOM.nextInt(1000);
                    delayTick = 0;

                    if (!this.hasLid()) {
                        if (this.fluid == null || this.fluid.getFluid() == FluidRegistry.WATER) {
                            this.addFluid(new FluidStack(FluidRegistry.WATER, MainSettingsStatic.barrelRain));
                        }
                        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    }
                } else {
                    delayTick++;
                }
            }
        }

        if (getBlockMetadata() == 0 && this.fluid != null && this.fluid.getFluid() == FluidRegistry.LAVA) {

            processTimeElapsed++;
            if (processTimeElapsed == 60) {
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.flowing_lava);
                PacketHelper.playSound("random.fizz", this, 1, 1);
            }
        } else {
            if (getBlockMetadata() == 0) {
                BarrelWoodRecipe recipe = SubsistenceRecipes.BARREL.getWooden(fluid, contents);
                if (recipe != null) {
                    processTimeElapsed++;

                    if (processTimeElapsed >= recipe.getTime()) {
                        processTimeElapsed = 0;
                        contents = new ItemStack[0];
                        dumpFluid();
                        addItemToStack(recipe.getOutputItem());
                        addFluid(recipe.getOutputLiquid());
                    }
                }
            } else if (getBlockMetadata() == 1) {
                BarrelWoodRecipe recipe = SubsistenceRecipes.BARREL.getWooden(fluid, contents);
                if (recipe != null) {
                    processTimeElapsed++;

                    if (processTimeElapsed >= recipe.getTime()) {
                        processTimeElapsed = 0;
                        contents = new ItemStack[0];
                        dumpFluid();
                        addItemToStack(recipe.getOutputItem());
                        addFluid(recipe.getOutputLiquid());
                    }
                } else {
                    BarrelStoneRecipe recipe1 = SubsistenceRecipes.BARREL.getStone(fluid, contents);
                    if (recipe1 != null) {
                        updateTemperature(recipe1);
                        if (!needHeat || currentTemperature >= maxTemperature) {
                            if (recipe1.getTime() > 0) {
                                processTimeElapsed++;
                            } else {
                                processTimeElapsed = 0;
                            }
                            if (processTimeElapsed >= recipe1.getTime()) {
                                contents = new ItemStack[0];
                                dumpFluid();
                                addItemToStack(recipe1.getOutputItem());
                                addFluid(recipe1.getOutputLiquid());
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateTemperature(BarrelStoneRecipe recipe) {
        currentTemperature += checkHeatSource(recipe);
        if (recipe.getTimeFire() > -1 || recipe.getTimeLava() > -1 && recipe.getTimeTorch() > -1) {
            needHeat = true;
        }
        if (currentTemperature < 0F) {
            currentTemperature = 0F;
        }
    }

    private float checkHeatSource(BarrelStoneRecipe recipe) {

        if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == Blocks.fire) {
            return recipe.getTimeFire();
        } else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == Blocks.lava) {
            return recipe.getTimeLava();
        } else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) == Blocks.torch) {
            return recipe.getTimeTorch();
        } else {
            return -1F;
        }
    }

    public int getCapacity() {
        if (this.blockMetadata == 0) {
            return 2 * FluidContainerRegistry.BUCKET_VOLUME;
        } else {
            return 8 * FluidContainerRegistry.BUCKET_VOLUME;
        }
    }

    public boolean addItemToStack(ItemStack itemStack) {
        if (itemStack == null)
            return false;
        ArrayList<ItemStack> contentList;
        if (contents != null && contents.length > 0) {
            contentList = new ArrayList<ItemStack>(Arrays.asList(contents));
        } else {
            contentList = new ArrayList<ItemStack>();
        }

        if (contentList.size() == 2) {
            return false;
        }

        contentList.add(itemStack);
        contents = contentList.toArray(new ItemStack[contentList.size()]);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return true;
    }

    public ItemStack removeItemFromStack() {
        ArrayList<ItemStack> contentList = new ArrayList<ItemStack>(Arrays.asList(contents));
        ItemStack itemStack = contentList.remove(contentList.size() - 1);
        contents = contentList.toArray(new ItemStack[contentList.size()]);
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        return itemStack;
    }

    public boolean addFluid(FluidStack inFluid) {
        if (inFluid.getFluid() != null) {
            if (this.fluid == null) { //if currently empty
                this.fluid = inFluid;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            } else if (inFluid.fluidID == this.fluid.fluidID) { //if current fluid is same
                if (this.fluid.amount + inFluid.amount <= this.getCapacity()) { //dont overflow
                    this.fluid.amount = this.fluid.amount + inFluid.amount;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean reduceFluid(FluidStack inFluid) {
        if (this.fluid.amount - inFluid.amount == 0) {
            this.fluid = null;
            processTimeElapsed = 0;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        } else if (this.fluid.amount - inFluid.amount > 0) {
            this.fluid.amount = this.fluid.amount - inFluid.amount;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        }
        return false;
    }

    public void dumpFluid() {
        this.fluid = null;
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtRoot) {
        super.readCustomNBT(nbtRoot);
        if (nbtRoot.getBoolean("has_fluid")) {
            fluid = new FluidStack(FluidRegistry.getFluid(nbtRoot.getString("fluid_name")), nbtRoot.getInteger("fluid_amount"));
        } else {
            fluid = null;
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtRoot) {
        super.writeCustomNBT(nbtRoot);
        if (fluid != null && fluid.amount > 0) {
            nbtRoot.setBoolean("has_fluid", true);
            nbtRoot.setString("fluid_name", FluidRegistry.getFluidName(this.fluid.fluidID));
            nbtRoot.setInteger("fluid_amount", this.fluid.amount);
        }
    }
}