package subsistence.common.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import subsistence.client.handler.ClientTimerHandler;
import subsistence.common.network.PacketHandler;

/**
 * @author dmillerw
 */
public class PacketUpdateTimer implements IMessage {

    public static void startTimer(EntityPlayer entityPlayer, String tag, int duration) {
        PacketUpdateTimer packet = new PacketUpdateTimer();
        packet.tag = tag;
        packet.duration = duration;
        PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) entityPlayer);
    }

    public static void stopTimer(EntityPlayer entityPlayer) {
        PacketUpdateTimer packet = new PacketUpdateTimer();
        packet.tag = "";
        packet.duration = 0;
        PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) entityPlayer);
    }

    public String tag;
    public int duration;

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, tag);
        buf.writeInt(duration);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readUTF8String(buf);
        duration = buf.readInt();
    }

    public static class Handler implements IMessageHandler<PacketUpdateTimer, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateTimer message, MessageContext ctx) {
            if (message.tag == null || message.tag.isEmpty()) {
                ClientTimerHandler.INSTANCE.stop();
            } else {
                ClientTimerHandler.INSTANCE.update(message.tag, 0, message.duration);
            }
            return null;
        }
    }
}
