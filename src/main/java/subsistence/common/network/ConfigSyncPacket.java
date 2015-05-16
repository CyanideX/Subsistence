package subsistence.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class ConfigSyncPacket implements IMessageHandler<ConfigSyncPacket.ConfigSyncMessage,IMessage> {
    @Override
    public IMessage onMessage (ConfigSyncMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) { //only get values on the server from the client
            //TODO: log
            //TODO: client.value = message.value
        }
        return null;
    }
    public static class ConfigSyncMessage implements IMessage {
        private int value;
        public ConfigSyncMessage () {}
        public ConfigSyncMessage (int value) {
            this.value = value;
        }
        @Override
        public void fromBytes (ByteBuf buf) {
            //ORDER MATTERS, DONT SCREW IT UP
            this.value = buf.readInt();
        }
        @Override
        public void toBytes (ByteBuf buf) {
            buf.writeInt(value);
        }
    }
}
