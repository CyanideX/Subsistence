package com.subsistence;

import com.subsistence.common.command.CommandSubsistence;
import com.subsistence.common.command.CommandTPX;
import com.subsistence.common.network.PacketFX;
import com.subsistence.common.network.PacketSyncContents;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import com.subsistence.common.lib.SubsistenceProps;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

import java.io.File;

/**
 * @author Royalixor.
 */
@Mod(modid = SubsistenceProps.ID, name = SubsistenceProps.NAME, version = SubsistenceProps.VERSION, dependencies = SubsistenceProps.DEPENDENCIES)
public class Subsistence {

    public static SimpleNetworkWrapper network;

    @Mod.Instance(SubsistenceProps.ID)
    public static Subsistence instance;

    @SidedProxy(clientSide = SubsistenceProps.CLIENT, serverSide = SubsistenceProps.SERVER)
    public static CommonProxy proxy;

    public static String configPath;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel("subsistence");
        network.registerMessage(PacketFX.PacketFXHandler.class, PacketFX.class, 0, Side.CLIENT);
        network.registerMessage(PacketSyncContents.PacketSyncContentsHandler.class, PacketSyncContents.class, 1, Side.CLIENT);
        network.registerMessage(PacketSyncContents.PacketSyncContentsHandler.class, PacketSyncContents.class, 1, Side.SERVER);

        configPath = event.getModConfigurationDirectory().getPath() + File.separator + "/Subsistence/";

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSubsistence());
        event.registerServerCommand(new CommandTPX());

    }
}
