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
import subsistence.common.recipe.core.RecipeParser;
import subsistence.common.recipe.wrapper.module.core.ModularRegistry;
import subsistence.common.util.EventUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Royalixor.
 */
public class CommonProxy {

    /*public static class Recipe {
        @RestrictedType({"generic.item", "generic.fluid"})
        public ModularObject input[];
        @RestrictedType({"generic.fluid"})
        public ModularObject process;
        public ModularObject output;
    }*/

    public void preInit() {
        SubsistenceFluids.initializeFluids();
        SubsistenceBlocks.initialize();
        SubsistenceItems.initialize();
        SubsistenceFluids.initializeFluidContainers();
        SubsistenceRecipes.initialize();

        AbstractSerializer.initialize();
        ModularRegistry.initialize();

        /*Recipe[] recipes = JsonUtil.gson().fromJson("[\n" +
                "    {\n" +
                "        \"input\": [\n" +
                "            {\n" +
                "                \"object\": \"generic.fluid\",\n" +
                "                \"data\": {\n" +
                "                    \"fluid\": \"water\",\n" +
                "                    \"amount\": 450\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"object\": \"generic.item\",\n" +
                "                \"data\": {\n" +
                "                    \"item\": \"stone\",\n" +
                "                    \"amount\": 5\n" +
                "                }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"process\": {\n" +
                "            \"object\": \"barrel.process.mix\"\n" +
                "        },\n" +
                "        \"output\": {\n" +
                "            \"object\": \"generic.fluid\",\n" +
                "            \"data\": {\n" +
                "                \"fluid\": \"lava\",\n" +
                "                \"amount\": 666\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"input\": [\n" +
                "            {\n" +
                "                \"object\": \"generic.fluid\",\n" +
                "                \"data\": {\n" +
                "                    \"fluid\": \"lava\",\n" +
                "                    \"amount\": 450\n" +
                "                }\n" +
                "            },\n" +
                "            {\n" +
                "                \"object\": \"generic.item\",\n" +
                "                \"data\": {\n" +
                "                    \"item\": \"grass\",\n" +
                "                    \"amount\": 1\n" +
                "                }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"process\": {\n" +
                "            \"object\": \"barrel.process.melt\",\n" +
                "            \"data\": {\n" +
                "                \"time\": 500,\n" +
                "                \"heat\": 450\n" +
                "            }\n" +
                "        },\n" +
                "        \"output\": {\n" +
                "            \"object\": \"generic.item\",\n" +
                "            \"data\": {\n" +
                "                \"item\": \"stone\",\n" +
                "                \"amount\": 64\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "]", Recipe[].class);

        for (Recipe recipe : recipes) {
            RecipeParser.checkTypes(recipe);
        }

        System.out.println(recipes);*/

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

        try { //TODO: add config option for dumping this as it adds an extra 2.3 seconds to the loading time for this
            RecipeParser.dumpItems(new File(Subsistence.configPath, "key_dump.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

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
