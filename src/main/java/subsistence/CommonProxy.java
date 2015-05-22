package subsistence;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import subsistence.common.block.SubsistenceBlocks;
import subsistence.common.config.ConfigManager;
import subsistence.common.core.handler.*;
import subsistence.common.fluid.SubsistenceFluids;
import subsistence.common.item.ItemHammer;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.network.nbt.data.AbstractSerializer;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.util.EventUtil;

public class CommonProxy {

    public void preInit() {
        SubsistenceFluids.initializeFluids();
        SubsistenceBlocks.initialize();
        SubsistenceItems.initialize();
        SubsistenceFluids.initializeFluidContainers();
        SubsistenceRecipes.initialize();

        AbstractSerializer.initialize();

        NetworkRegistry.INSTANCE.registerGuiHandler(Subsistence.instance, new GuiHandler());

        EventUtil.register(new BlockEventHandler(), EventUtil.Type.FML);
        EventUtil.register(new SpiderTracker(), EventUtil.Type.BOTH);
        EventUtil.register(BucketHandler.INSTANCE, EventUtil.Type.FORGE);
        EventUtil.register(new WebHandler(), EventUtil.Type.FORGE);
    }

    public void init() {
    }

    public void postInit() {

        ConfigManager.loadAllFiles();

        // Tool registration
        ToolDefinition.register(new ItemStack(SubsistenceItems.hammerWood), ToolDefinition.HAMMER, ItemHammer.STRENGTH[0]);
        ToolDefinition.register(new ItemStack(SubsistenceItems.hammerStone), ToolDefinition.HAMMER, ItemHammer.STRENGTH[1]);
        ToolDefinition.register(new ItemStack(SubsistenceItems.hammerIron), ToolDefinition.HAMMER, ItemHammer.STRENGTH[2]);
        ToolDefinition.register(new ItemStack(SubsistenceItems.hammerSteel), ToolDefinition.HAMMER, ItemHammer.STRENGTH[3]);
        ToolDefinition.register(new ItemStack(SubsistenceItems.hammerDiamond), ToolDefinition.HAMMER, ItemHammer.STRENGTH[4]);

        // Axe registration
        for (Object key : GameData.getItemRegistry().getKeys()) {
            Item axe = (Item) Item.itemRegistry.getObject(key.toString());

            if (axe instanceof ItemAxe) {
                ToolDefinition.register(new ItemStack(axe), ToolDefinition.AXE, ((ItemTool) axe).func_150913_i().getEfficiencyOnProperMaterial());
            }
        }
    }
}
