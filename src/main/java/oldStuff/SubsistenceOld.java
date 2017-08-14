package oldStuff;

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
import net.minecraftforge.fml.common.SidedProxy;
import oldStuff.common.command.CommandSubsistence;
import oldStuff.common.command.CommandTPX;
import oldStuff.common.config.CoreSettings;
import oldStuff.common.network.PacketHandler;
import oldStuff.common.network.UpdateChecker;

import java.io.File;

// @Mod(modid = SubsistenceOld.MODID, name = SubsistenceOld.NAME, version = SubsistenceOld.VERSION, dependencies = "required-after:Forge@[%FORGE_VERSION%,)")
public class SubsistenceOld {

    public static final String MODID = "subsistence";
    public static final String NAME = "SubsistenceOld";
    public static final String VERSION = "Alpha";
    public static final String RESOURCE_PREFIX = "subsistence:";

    @Mod.Instance(SubsistenceOld.MODID)
    public static SubsistenceOld instance;

    @SidedProxy(clientSide = "oldStuff.ClientProxy", serverSide = "oldStuff.CommonProxy")
    public static CommonProxy proxy;

    public static String configPath;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(instance);
        PacketHandler.initialize();
        configPath = event.getModConfigurationDirectory().getPath() + File.separator + "/SubsistenceOld/";
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
        if (CoreSettings.updateChecker) {
            UpdateChecker.checkForUpdate();
        }
    }

    @SubscribeEvent
    public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        //this comment was created in the 100th commit. HAHA I STOLE IT FROM YOU DYLAN
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP) {
                if (CoreSettings.updateChecker) {
                    if (UpdateChecker.updateAvaliable) {
                        event.player.addChatMessage(new ChatComponentText("§dUpdate for SubsistenceOld is available!"));
                    }
                }
            }
        }
    }
}
