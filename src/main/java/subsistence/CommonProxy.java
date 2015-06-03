package subsistence;

import cpw.mods.fml.common.network.NetworkRegistry;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.config.ConfigManager;
import subsistence.common.core.handler.*;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.network.nbt.data.AbstractSerializer;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.util.EventUtil;

public class CommonProxy {

    public void preInit() {
        SubsistenceItems.initialize();
        SubsistenceFluids.initializeFluids();

        EventUtil.register(new FluidHandler(), EventUtil.Type.FORGE);

        SubsistenceBlocks.initialize();
        SubsistenceFluids.initializeFluidContainers();
        SubsistenceRecipes.initialize();

        AbstractSerializer.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(Subsistence.instance, new GuiHandler());

        EventUtil.register(new SpiderTracker(), EventUtil.Type.BOTH);
        EventUtil.register(new WebHandler(), EventUtil.Type.FORGE);
        EventUtil.register(new BoilingWaterHandler(), EventUtil.Type.FORGE);
    }

    public void init() {

    }

    public void postInit() {
        ConfigManager.loadAllFiles();
    }
}
