package subsistence.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import subsistence.Subsistence;
import subsistence.common.network.packet.*;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Subsistence.NAME);

    public static void initialize() {
        INSTANCE.registerMessage(PacketStopTimer.Handler.class, PacketStopTimer.class, 1, Side.SERVER);

        INSTANCE.registerMessage(PacketSyncConfig.Handler.class, PacketSyncConfig.class, -1, Side.CLIENT);
        INSTANCE.registerMessage(PacketFX.Handler.class, PacketFX.class, -2, Side.CLIENT);
        INSTANCE.registerMessage(PacketUpdateTimer.Handler.class, PacketUpdateTimer.class, -3, Side.CLIENT);
        INSTANCE.registerMessage(PacketForcePlayerSlot.Handler.class, PacketForcePlayerSlot.class, -4, Side.CLIENT);
    }
}
