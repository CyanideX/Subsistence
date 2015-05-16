package subsistence;

import subsistence.common.command.CommandSubsistence;
import subsistence.common.command.CommandTPX;
import subsistence.common.network.PacketFX;
import subsistence.common.network.PacketHandler;
import subsistence.common.network.PacketSyncContents;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
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
@Mod(modid = Subsistence.MODID, name = Subsistence.NAME, version = Subsistence.VERSION, dependencies = "required-after:Forge@[%FORGE_VERSION%,)")
public class Subsistence {

    public static final String MODID = "subsistence";
    public static final String NAME = "Subsistence";
    public static final String VERSION = "Alpha";
    public static final String RESOURCE_PREFIX = "subsistence:";

    public static SimpleNetworkWrapper network;

    @Mod.Instance(Subsistence.MODID)
    public static Subsistence instance;

    @SidedProxy(clientSide = "subsistence.ClientProxy", serverSide = "subsistence.CommonProxy")
    public static CommonProxy proxy;

    public static String configPath;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        PacketHandler.initPackets();

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
