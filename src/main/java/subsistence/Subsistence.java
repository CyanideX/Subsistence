package subsistence;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import subsistence.common.command.CommandSubsistence;
import subsistence.common.command.CommandTPX;
import subsistence.common.network.PacketHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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
    @SubscribeEvent
    public void playerLoggedIn (PlayerEvent.PlayerLoggedInEvent event) {
        //TODO: send clients the values in server configs
    }
}
