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
import subsistence.common.network.PacketHandler;

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
    public void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        //    just for you dylan  ^
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP) {
                if (event.player.getDisplayName().equalsIgnoreCase("CyanideX")) {
                    event.player.addChatMessage(new ChatComponentText("<CyanideX> I'm not so sure that text is such a bad idea Matt."));
                } else if (event.player.getDisplayName().equalsIgnoreCase("Yulife")) {
                    event.player.addChatMessage(new ChatComponentText("<CyanideX> It's Yulife\n" +
                            "<Inap> no\n" +
                            "<CyanideX> Yeah it is"));
                } else if (event.player.getDisplayName().equalsIgnoreCase("MattDahEpic")) {
                    event.player.addChatComponentMessage(new ChatComponentText("Matt is so great. G. R. A. T."));
                } else if (event.player.getDisplayName().equalsIgnoreCase("dmillerw")) {
                    event.player.addChatComponentMessage(new ChatComponentText("public static void parseMainSettings (File file)"));
                } else if (event.player.getDisplayName().equalsIgnoreCase("BuhneBatVampire")) {
                    event.player.addChatComponentMessage(new ChatComponentText("You have played 72 hours of osu! today."));
                } else if (event.player.getDisplayName().equalsIgnoreCase("lclc98")) {
                    event.player.addChatComponentMessage(new ChatComponentText("What does func_149851_a do?"));
                } else if (event.player.getDisplayName().equalsIgnoreCase("FireBall1725")) {
                    event.player.addChatComponentMessage(new ChatComponentText("Given [Fire Charge] * 1725 to FireBall1725"));
                }
            }
        }
    }
}
