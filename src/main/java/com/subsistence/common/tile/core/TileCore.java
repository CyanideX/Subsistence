package com.subsistence.common.tile.core;

import com.subsistence.common.network.VanillaPacketHelper;
import com.subsistence.common.network.nbt.NBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author dmillerw
 */
public abstract class TileCore extends TileEntity {

    private static final int DESCRIPTION_PACKET = 0;
    private static final int POKE_PACKET = 1;

    public void writeCustomNBT(NBTTagCompound nbt) {
    }

    public void readCustomNBT(NBTTagCompound nbt) {
    }

    public String[] descriptionPacketFields() {
        return handler.getDescriptionFields();
    }

    public void onPoked() {
    }

    public void onBlockBroken() {
    }

    protected NBTHandler handler;

    public TileCore() {
        this(true);
    }

    public TileCore(boolean scan) {
        handler = new NBTHandler(this, scan);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        handler.writeAllToNBT(nbt);
        writeCustomNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        handler.readFromNBT(nbt);
        readCustomNBT(nbt);
    }

    private void writeDescriptionPacket(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        handler.writeSelectedToNBT(descriptionPacketFields(), nbt);
        writeCustomNBT(nbt);
    }

    private void readDescriptionPacket(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        handler.readSelectedFromNBT(descriptionPacketFields(), nbt);
        readCustomNBT(nbt);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeDescriptionPacket(tag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, DESCRIPTION_PACKET, tag);
    }

    public void markForUpdate() {
        if (hasWorldObj()) {
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    public void markForRenderUpdate() {
        if (hasWorldObj()) {
            getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        }
    }

    public void updateNeighbors() {
        if (hasWorldObj()) {
            getWorldObj().notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        }
    }

    public void sendPoke() {
        VanillaPacketHelper.sendToAllWatchingTile(this, new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, POKE_PACKET, new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        switch (pkt.func_148853_f()) {
            case DESCRIPTION_PACKET:
                readDescriptionPacket(pkt.func_148857_g());
                break;
            case POKE_PACKET:
                onPoked();
                break;
            default:
                break;
        }
        markForRenderUpdate();
    }
}
