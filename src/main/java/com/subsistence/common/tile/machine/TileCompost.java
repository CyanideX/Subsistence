package com.subsistence.common.tile.machine;

import com.subsistence.common.network.nbt.NBTHandler;
import com.subsistence.common.tile.core.TileCoreMachine;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import com.subsistence.common.lib.StackReference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Thlayli
 */
public class TileCompost extends TileCoreMachine {

    public final float maxAngle = -45f;
    public final float minAngle = 14f;

    public final float maxTemperature = 32F;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public boolean lidOpen = true;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public FluidStack fluid;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    public ItemStack[] binContents;

    @NBTHandler.NBTData
    public int processTimeElapsed = 0;

    @NBTHandler.NBTData
    public float currentTemperature = 0F;

    @SideOnly(Side.CLIENT)
    public float currentAngle = 0f;

    public TileCompost(){
        super();
        binContents = new ItemStack[0];
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote) {
            updateLid();
        }
        else{
            updateTemperature();
            processContents();
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

    private void updateTemperature(){
        currentTemperature += checkHeatSource();
        if(currentTemperature > maxTemperature){
            currentTemperature = maxTemperature;
        }
        if(currentTemperature < 0F){
            currentTemperature = 0F;
        }
    }


    private void processContents(){
        if(fluid != null) {
            if (findItemInStack(StackReference.DIRT) && findItemInStack(StackReference.WEB) && fluid.getFluid() == FluidRegistry.WATER) {
                if (processTimeElapsed <= 5400 && !lidOpen) {
                    processTimeElapsed++;
                }

                if (processTimeElapsed == 5400) {
                    removeItemFromStack();
                    removeItemFromStack();
                    removeFluid();
                    addItemToStack(StackReference.MYCELIUM.copy());
                }
            }
        }
        else {
            if (findItemInStack(StackReference.DIRT) && binContents.length == 1) {
                if (processTimeElapsed <= 540 && currentTemperature == maxTemperature && !lidOpen) {
                    processTimeElapsed++;
                }

                if (processTimeElapsed == 540) {
                    removeItemFromStack();
                    addItemToStack(StackReference.SAND.copy());
                    addFluid(new FluidStack(FluidRegistry.WATER, FluidContainerRegistry.BUCKET_VOLUME));
                }
            }
        }
    }

    private float checkHeatSource(){

        if(worldObj.getBlock(xCoord,yCoord - 1,zCoord) == Blocks.fire){
            return 1f;
        }
        if(worldObj.getBlock(xCoord,yCoord - 1,zCoord) == Blocks.lava){
            return 0.5f;
        }
        else
        {
            return -1F;
        }
    }

    public boolean addItemToStack(ItemStack itemStack){
        ArrayList<ItemStack> contentList;
        if(binContents != null && binContents.length > 0){
            contentList = new ArrayList<ItemStack>(Arrays.asList(binContents));
        }
        else {
            contentList = new ArrayList<ItemStack>();
        }

        if(contentList.size() == 2){
            return false;
        }

        contentList.add(itemStack);
        binContents = contentList.toArray(new ItemStack[contentList.size()]);
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return true;
    }

    public ItemStack removeItemFromStack(){
        ArrayList<ItemStack> contentList = new ArrayList<ItemStack>(Arrays.asList(binContents));
        ItemStack itemStack = contentList.remove(contentList.size() - 1);
        binContents = contentList.toArray(new ItemStack[contentList.size()]);
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
        return itemStack;
    }

    private boolean findItemInStack(ItemStack itemStack){
        for(int i = 0; i < binContents.length;i++){
            if(binContents[i].isItemEqual(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public void addFluid(FluidStack fluid){
        this.fluid = fluid;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }

    public void removeFluid(){
        this.fluid = null;
        processTimeElapsed = 0;
        worldObj.markBlockForUpdate(xCoord,yCoord,zCoord);
    }




}