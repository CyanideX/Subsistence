package subsistence.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import subsistence.Subsistence;
import subsistence.common.network.packet.PacketSyncConfig;
import subsistence.common.network.packet.PacketFX;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Subsistence.NAME);

    public static void initialize() {
        INSTANCE.registerMessage(PacketSyncConfig.Handler.class, PacketSyncConfig.class, -1, Side.CLIENT);
        INSTANCE.registerMessage(PacketFX.Handler.class, PacketFX.class, -2, Side.CLIENT);
    }
}
