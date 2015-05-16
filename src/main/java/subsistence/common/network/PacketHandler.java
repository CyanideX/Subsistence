package subsistence.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import subsistence.Subsistence;

public class PacketHandler {
    public static SimpleNetworkWrapper net;
    public static void initPackets () {
        net = NetworkRegistry.INSTANCE.newSimpleChannel(Subsistence.NAME.toUpperCase());
        registerMessage(ConfigSyncPacket.class, ConfigSyncPacket.ConfigSyncMessage.class);
        //TODO: fix teh following, its probably wrong
        registerMessage(PacketFX.PacketFXHandler.class, PacketFX.class);
        registerMessage(PacketSyncContents.PacketSyncContentsHandler.class,PacketSyncContents.class);
    }
    public static int nextPacketId = 0;
    public static void registerMessage (Class packet, Class message) {
        net.registerMessage(packet,message,nextPacketId,Side.CLIENT);
        net.registerMessage(packet,message,nextPacketId,Side.SERVER);
        nextPacketId++;
    }
}
