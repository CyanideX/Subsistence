package com.subsistence.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("subsistence");

    public static void initialize() {
        INSTANCE.registerMessage(PacketFX.class, PacketFX.class, 0, Side.CLIENT);
    }
}
