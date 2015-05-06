package com.subsistence.common.network;

import com.subsistence.common.tile.core.TileCore;
import com.subsistence.common.tile.machine.TileHammerMill;
import com.subsistence.common.tile.machine.TileMetalPress;
import com.subsistence.common.tile.machine.TileTable;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author lclc98
 */
public class PacketSyncContents implements IMessage {

    public ItemStack itemStack;
    private int xCoord;
    private int yCoord;
    private int zCoord;

    public PacketSyncContents() {

    }

    public PacketSyncContents(TileCore tile, ItemStack itemStack) {
        this.xCoord = tile.xCoord;
        this.yCoord = tile.yCoord;
        this.zCoord = tile.zCoord;
        this.itemStack = itemStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        xCoord = ByteBufUtils.readVarInt(buf, 5);
        yCoord = ByteBufUtils.readVarInt(buf, 5);
        zCoord = ByteBufUtils.readVarInt(buf, 5);
        itemStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeVarInt(buf, xCoord, 5);
        ByteBufUtils.writeVarInt(buf, yCoord, 5);
        ByteBufUtils.writeVarInt(buf, zCoord, 5);
        ByteBufUtils.writeItemStack(buf, itemStack);
    }

    public static class PacketSyncContentsHandler implements IMessageHandler<PacketSyncContents, IMessage> {

        @Override
        public IMessage onMessage(PacketSyncContents packet, MessageContext ctx) {

            EntityPlayer player = (ctx.side == Side.CLIENT) ? Minecraft.getMinecraft().thePlayer : ctx.getServerHandler().playerEntity;
            TileCore tile = (TileCore) player.worldObj.getTileEntity(packet.xCoord, packet.yCoord, packet.zCoord);
            if (tile != null) {
                if (tile instanceof TileTable) {
                    ((TileTable) tile).stack = packet.itemStack;
                } else if (tile instanceof TileMetalPress) {
                    ((TileMetalPress) tile).itemStack = packet.itemStack;
                }
            }
            return null;
        }
    }
}
