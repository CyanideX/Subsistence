package subsistence.common.tile.machine;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.config.MainSettingsStatic;
import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.CompostRecipe;
import subsistence.common.tile.core.TileCoreMachine;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Thlayli
 */
public class TileCompost extends TileCoreMachine {

    public final float maxAngle = -45f;
    public final float minAngle = 14f;

    public boolean needHeat = false;
    public final float maxTemperature = 32;

    @NBTHandler.Sync(true)
    public boolean lidOpen = true;

    @NBTHandler.Sync(true)
    public FluidStack fluid;

    @NBTHandler.Sync(true)
    public ItemStack[] contents;

    public int processTimeElapsed = 0;
    public float currentTemperature = 0F;
    public float currentAngle = 0f;

    public TileCompost() {
        super();
        contents = new ItemStack[0];
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            updateLid();
        } else {
            CompostRecipe recipe = SubsistenceRecipes.COMPOST.get(fluid, contents);
            if (recipe != null) {
                updateTemperature(recipe);
                processContents(recipe);
            }
        }

    }

    private void updateLid() {
        currentAngle += lidOpen ? -4f : 4f;
        if (currentAngle <= maxAngle) {
            currentAngle = maxAngle;
        }
        if (currentAngle >= minAngle) {
            currentAngle = minAngle;
        }
    }

    public int getVolume () {
        return MainSettingsStatic.compostBucketSize*FluidContainerRegistry.BUCKET_VOLUME; //TODO: config value
    }


    private void updateTemperature(CompostRecipe recipe) {
        currentTemperature += checkHeatSource(recipe);
        if (recipe.getTimeFire() > -1 || recipe.getTimeLava() > -1 && recipe.getTimeTorch() > -1) {
            needHeat = true;
        }
        if (currentTemperature < 0F) {
            currentTemperature = 0F;
        }
    }


    private void processContents(CompostRecipe recipe) {

        if (recipe.valid(fluid, contents)) {
            if (!needHeat || currentTemperature >= maxTemperature) {
                if (recipe.getTime() > 0) {
                    processTimeElapsed++;
                } else {
                    processTimeElapsed = 0;
                }
                if (processTimeElapsed >= recipe.getTime()) {
                    contents = new ItemStack[0];
                    voidFluid();
                    addItemToStack(recipe.getOutputItem());
                    increaseFluid(recipe.getOutputLiquid());
                }
            }
        }

    }

    private float checkHeatSource(CompostRecipe recipe) {

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

    public boolean increaseFluid(FluidStack inFluid) {
        if (inFluid != null) {
            if (this.fluid == null) { //if there's nothing here
                this.fluid = inFluid;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            } else if (inFluid.fluidID == this.fluid.fluidID) { //if the same fluid
                if (this.fluid.amount + inFluid.amount < this.getVolume()) { //if you can hold it
                    this.fluid.amount = this.fluid.amount + inFluid.amount;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean decreaseFluid(FluidStack inFluid) {
        if (inFluid != null) { //if you're not clicking with null
            if (inFluid.fluidID == this.fluid.fluidID) { //and it's the same type
                if (this.fluid.amount - inFluid.amount > 0) { //and there's more than enough water
                    processTimeElapsed = 0;
                    this.fluid.amount = this.fluid.amount - inFluid.amount;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    return true;
                } else if (this.fluid.amount - inFluid.amount == 0) { //exactly enough water
                    this.fluid = null;
                    processTimeElapsed = 0;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    return true;
                }
            }
        }
        return false;
    }
    public void voidFluid() {
        this.fluid = null;
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
}