package subsistence;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import subsistence.common.command.CommandSubsistence;
import subsistence.common.command.CommandTPX;
import subsistence.common.config.MainSettingsStatic;
import subsistence.common.network.PacketHandler;
import subsistence.common.network.UpdateChecker;

import java.io.File;

/**
 * @author Royalixor.
 * @author MattDahEpic
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
        FMLCommonHandler.instance().bus().register(instance);
        PacketHandler.initialize();
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
        if (MainSettingsStatic.updateChecker) {
            UpdateChecker.checkForUpdate();
        }
    }

    @SubscribeEvent
    public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        //this comment was created in the 100th commit. HAHA I STOLE IT FROM YOU DYLAN
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP) {
                if (MainSettingsStatic.updateChecker) {
                    if (UpdateChecker.updateAvaliable) {
                        event.player.addChatMessage(new ChatComponentText("§dUpdate for Subsistence is available!"));
                    }
                }
            }
        }
    }
}
