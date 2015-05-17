package subsistence;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
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
        //just for you dylan  ^
        if (event.player.getUniqueID().equals("54972f65-b920-4a60-b539-dc443c16cd29")) { //CyanideX
            event.player.addChatMessage(new ChatComponentText("<CyanideX> I'm not so sure that text is such a bad idea Matt."));
        } else if (event.player.getUniqueID().equals("315f746e-8e38-44c5-b604-fd4b7014ec8c")) { //Yulfie
            event.player.addChatMessage(new ChatComponentText("<CyanideX> It's Yulife\n" +
                    "<Inap> no\n" +
                    "<CyanideX> Yeah it is"));
        } else if (event.player.getUniqueID().equals("c715991d-e69c-48f9-a92d-8fc60c0829fb")) { //MattDahEpic
            event.player.addChatComponentMessage(new ChatComponentText("Matt is so great. G. R. A. T."));
        } else if (event.player.getUniqueID().equals("f579a27a-d06b-46a7-9f90-f77af54a108c")) { //dmillerw
            event.player.addChatComponentMessage(new ChatComponentText("public static void parseMainSettings (File file)"));
        } else if (event.player.getUniqueID().equals("dc697d82-dc39-4e11-814b-0a788f592842")) { //BuhneBatVampire
            event.player.addChatComponentMessage(new ChatComponentText("You have played 72 hours of osu! today."));
        } else if (event.player.getUniqueID().equals("f0f76db6-0461-4151-8ba7-392d65d62ea3")) { //lclc98
            event.player.addChatComponentMessage(new ChatComponentText("What does func_149851_a do?"));
        } else if (event.player.getUniqueID().equals("e43e9766-f903-48e1-818f-d41bb48d80d5")) { //FireBall1725
            event.player.addChatComponentMessage(new ChatComponentText("Given [Fire Charge] * 1725 to FireBall1725"));
        }
    }
}
