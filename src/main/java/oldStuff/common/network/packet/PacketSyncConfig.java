package oldStuff.common.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketSyncConfig implements IMessage {

    private int value;

    public PacketSyncConfig() {
    }

    public PacketSyncConfig(int value) {
        this.value = value;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        //ORDER MATTERS, DONT SCREW IT UP
        this.value = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        //ORDER STILL MATTERS
        buf.writeInt(value);
    }

    public static class Handler implements IMessageHandler<PacketSyncConfig, IMessage> {

        @Override
        public IMessage onMessage(PacketSyncConfig message, MessageContext ctx) {
            if (ctx.side.isClient()) { //only get values on the server from the client
                //TODO: log
                //TODO: client.value = message.value
            }
            return null;
        }
    }
}
