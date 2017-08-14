package oldStuff.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import oldStuff.common.core.handler.TimerHandler;

/**
 * @author dmillerw
 */
public class PacketStopTimer implements IMessage {

    @Override
    public void toBytes(ByteBuf buf) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketStopTimer, IMessage> {

        @Override
        public IMessage onMessage(PacketStopTimer message, MessageContext ctx) {
            TimerHandler.INSTANCE.stopTimer(ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}
