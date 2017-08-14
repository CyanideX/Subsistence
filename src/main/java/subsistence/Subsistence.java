package subsistence;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import subsistence.proxy.CommonProxy;

@Mod.EventBusSubscriber
@Mod(modid = Subsistence.MODID, version = Subsistence.VERSION, acceptedMinecraftVersions = "{1.12,1.13)")
public class Subsistence {
    public static final String MODID = "atmtweaks";
    public static final String VERSION = "1.0";
    public static final CreativeTabs creativeTab = new SubsistenceCreativeTab();
    @SidedProxy(clientSide = "atm.bloodworkxgaming.atmtweaks.proxy.ClientProxy", serverSide = "atm.bloodworkxgaming.atmtweaks.proxy.ServerProxy")
    public static CommonProxy proxy;
    @Mod.Instance(MODID)
    public static Subsistence instance;

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        proxy.registerModels(event);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
