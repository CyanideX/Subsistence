package com.subsistence.common.tile.machine;

import com.subsistence.common.network.VanillaPacketHelper;
import com.subsistence.common.network.nbt.NBTHandler;
import com.subsistence.common.tile.core.TileCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.FluidEvent.FluidDrainingEvent;
import net.minecraftforge.fluids.FluidEvent.FluidFillingEvent;

public final class TileBarrel extends TileCore implements IInventory, IFluidTank {
    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    private FluidStack fluid;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    private ItemStack[] input = new ItemStack[2];

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    private boolean hasLid;

    @NBTHandler.NBTData
    @NBTHandler.DescriptionData
    private int lavaTick;

    public ItemStack[] getInput() {
        return input;
    }

    public void setInput(ItemStack[] input) {
        this.input = input;
    }

    public void setFluid(FluidStack fluid) {
        this.fluid = fluid;
    }

    public boolean hasLid() {
        return this.hasLid;
    }

    public void setLid(boolean lid) {
        this.hasLid = lid;
        this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
    }

    public void toggleLid() {
        this.hasLid = !this.hasLid;
        this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (getBlockMetadata() == 0 && this.fluid != null && this.fluid.getFluid() == FluidRegistry.LAVA) {
            lavaTick++;
            if (worldObj.isAirBlock(xCoord, yCoord + 1, zCoord)) {
                worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.fire);
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + 1, this.zCoord);

            }
        }
        if (lavaTick == 60) {
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.flowing_lava);
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            VanillaPacketHelper.sendToAllWatchingTile(this, new S29PacketSoundEffect("random.fizz", this.xCoord, this.yCoord, this.zCoord, 1.0F, 1.0F));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound comp = new NBTTagCompound();
        this.writeToNBT(comp);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, comp);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pck) {
        this.readFromNBT(pck.func_148857_g());
    }

    @Override
    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public int getFluidAmount() {
        return this.fluid != null ? this.fluid.amount : 0;
    }

    @Override
    public int getCapacity() {
        if (getBlockMetadata() == 0)
            return 2 * FluidContainerRegistry.BUCKET_VOLUME;
        else
            return 8 * FluidContainerRegistry.BUCKET_VOLUME;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (this.fluid == null) {
                return Math.min(this.getCapacity(), resource.amount);
            }

            if (!this.fluid.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(this.getCapacity() - fluid.amount, resource.amount);
        }

        if (this.fluid == null) {
            this.fluid = new FluidStack(resource, Math.min(this.getCapacity(), resource.amount));
            FluidEvent.fireEvent(new FluidFillingEvent(fluid, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, this.fluid.amount));
            return fluid.amount;
        }

        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }

        int filled = this.getCapacity() - this.fluid.amount;

        if (resource.amount < filled) {
            this.fluid.amount += resource.amount;
            filled = resource.amount;
        } else {
            fluid.amount = this.getCapacity();
        }

        FluidEvent.fireEvent(new FluidFillingEvent(this.fluid, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, this.fluid.amount));
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (fluid == null) {
            return null;
        }

        int drained = maxDrain;
        if (this.fluid.amount < drained) {
            drained = this.fluid.amount;
        }

        FluidStack stack = new FluidStack(this.fluid, drained);
        if (doDrain) {
            this.fluid.amount -= drained;
            if (fluid.amount <= 0) {
                this.fluid = null;
            }

            FluidEvent.fireEvent(new FluidDrainingEvent(this.fluid, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, drained));
        }

        return stack;
    }

    @Override
    public void markDirty() {
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (this.getStackInSlot(i) != null && this.getStackInSlot(i).stackSize == 0) {
                this.setInventorySlotContents(i, null);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.input[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize > amount) {
                stack = stack.splitStack(amount);
                this.markDirty();
            } else {
                this.setInventorySlotContents(slot, null);
            }
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        if (this.getStackInSlot(slot) != null) {
            ItemStack stack = this.getStackInSlot(slot);
            this.setInventorySlotContents(slot, null);
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.input[slot] = stack;
    }

    @Override
    public String getInventoryName() {
        return "barrel_wood";
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
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }
}