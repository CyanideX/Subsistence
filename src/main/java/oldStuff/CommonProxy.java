package oldStuff;

import cpw.mods.fml.common.network.NetworkRegistry;
import oldStuff.common.block.SubsistenceBlocks;
import oldStuff.common.config.ConfigManager;
import oldStuff.common.core.handler.*;
import oldStuff.common.fluid.SubsistenceFluids;
import oldStuff.common.item.SubsistenceItems;
import oldStuff.common.network.nbt.data.AbstractSerializer;
import oldStuff.common.recipe.SubsistenceRecipes;
import oldStuff.common.util.EventUtil;

public class CommonProxy {

    public void preInit() {
        ConfigManager.preInit();
        
        SubsistenceFluids.initializeFluids();
        SubsistenceBlocks.initialize();
        SubsistenceItems.initialize();

        EventUtil.register(new FluidHandler(), EventUtil.Type.FORGE);

        SubsistenceFluids.initializeFluidContainers();
        SubsistenceRecipes.initialize();

        AbstractSerializer.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(SubsistenceOld.instance, new GuiHandler());

        EventUtil.register(new SpiderTracker(), EventUtil.Type.FORGE);
        EventUtil.register(new WebHandler(), EventUtil.Type.FORGE);
        EventUtil.register(new BoilingWaterHandler(), EventUtil.Type.BOTH);
        EventUtil.register(TimerHandler.INSTANCE, EventUtil.Type.FML);
    }

    public void init() {

    }

    public void postInit() {
        ConfigManager.postInit();
    }
}
