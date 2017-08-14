package oldStuff.common.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import oldStuff.common.network.PacketHandler;

/**
 * @author dmillerw
 */
public class PacketForcePlayerSlot implements IMessage {

    public static void forceSlot(EntityPlayer entityPlayer, int slot, ItemStack itemStack) {
        PacketForcePlayerSlot packet = new PacketForcePlayerSlot();
        packet.slot = slot;
        packet.itemStack = itemStack;
        PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) entityPlayer);
    }

    public int slot;
    public ItemStack itemStack;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        if (itemStack == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            ByteBufUtils.writeItemStack(buf, itemStack);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        if (buf.readBoolean()) {
            itemStack = ByteBufUtils.readItemStack(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketForcePlayerSlot, IMessage> {

        @Override
        public IMessage onMessage(PacketForcePlayerSlot message, MessageContext ctx) {
            Minecraft.getMinecraft().thePlayer.inventory.setInventorySlotContents(message.slot, message.itemStack);
            return null;
        }
    }
}
