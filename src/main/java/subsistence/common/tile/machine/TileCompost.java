package subsistence.common.tile.machine;

import subsistence.common.network.nbt.NBTHandler;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.CompostRecipe;
import subsistence.common.tile.core.TileCoreMachine;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public boolean lidOpen = true;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public FluidStack fluid;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public ItemStack[] contents;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public int processTimeElapsed = 0;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
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
                    removeFluid();
                    addItemToStack(recipe.getOutputItem());
                    addFluid(recipe.getOutputLiquid());
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
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return true;
    }

    public ItemStack removeItemFromStack() {
        ArrayList<ItemStack> contentList = new ArrayList<ItemStack>(Arrays.asList(contents));
        ItemStack itemStack = contentList.remove(contentList.size() - 1);
        contents = contentList.toArray(new ItemStack[contentList.size()]);
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return itemStack;
    }

    public void addFluid(FluidStack fluid) {
        this.fluid = fluid;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }

    public void removeFluid() {
        this.fluid = null;
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }


}