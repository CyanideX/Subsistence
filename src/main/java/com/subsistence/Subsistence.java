package com.subsistence;

import com.subsistence.common.command.CommandSubsistence;
import com.subsistence.common.network.PacketHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import com.subsistence.common.lib.SubsistenceProps;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * @author Royalixor.
 */
@Mod(modid = SubsistenceProps.ID, name = SubsistenceProps.NAME, version = SubsistenceProps.VERSION, dependencies = SubsistenceProps.DEPENDENCIES)
public class Subsistence {

    @Mod.Instance(SubsistenceProps.ID)
    public static Subsistence instance;

    @SidedProxy(clientSide = SubsistenceProps.CLIENT, serverSide = SubsistenceProps.SERVER)
    public static CommonProxy proxy;

    public static String configPath;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configPath = event.getModConfigurationDirectory().getPath() + File.separator + "/Subsistence/";

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PacketHandler.initialize();
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSubsistence());
    }
}
