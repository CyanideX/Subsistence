package oldStuff.common.tile.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import oldStuff.common.network.nbt.NBTHandler;

import java.util.Random;

public abstract class TileCore extends TileEntity {

    private static final int DESCRIPTION_PACKET = 0;

    protected final Random RANDOM;

    public void writeCustomNBT(NBTTagCompound nbt) {

    }

    public void readCustomNBT(NBTTagCompound nbt) {

    }

    public String[] descriptionPacketFields() {
        return handler.getDescriptionFields();
    }

    public void onBlockBroken() {

    }

    protected NBTHandler handler;

    public TileCore() {
        this(true);
    }

    public TileCore(boolean scan) {
        handler = new NBTHandler(this, scan);

        // One Random instance per tile instance
        this.RANDOM = new Random();
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

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        switch (pkt.func_148853_f()) {
            case DESCRIPTION_PACKET:
                readDescriptionPacket(pkt.func_148857_g());
                break;
            default:
                break;
        }
        markForRenderUpdate();
    }
}
