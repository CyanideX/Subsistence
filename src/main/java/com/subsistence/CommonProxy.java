package com.subsistence;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.core.handler.*;
import com.subsistence.common.fluid.SubsistenceFluids;
import com.subsistence.common.item.SubsistenceItems;
import com.subsistence.common.item.ItemHammer;
import com.subsistence.common.lib.tool.ToolDefinition;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.core.CompostParser;
import com.subsistence.common.recipe.core.GeneralParser;
import com.subsistence.common.recipe.core.RecipeParser;
import com.subsistence.common.recipe.manager.SieveManager;
import com.subsistence.common.util.EventUtil;
import com.subsistence.common.network.nbt.data.AbstractSerializer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.io.File;
import java.io.IOException;

/**
 * @author Royalixor.
 */
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

        loadJson();

        try {
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

    public static void loadJson() {
        File recipes = new File(Subsistence.configPath, "recipes/");
        if (!recipes.exists()) {
            recipes.mkdirs();
        }

        loadJson(recipes, "sieve", "");
        loadJson(recipes, "barrel", "");
        loadJson(recipes, "compost", "");
        loadJson(recipes, "metalpress", "");

        loadJson(recipes, "table", "hammer");
        loadJson(recipes, "table", "drying");
        loadJson(recipes, "table", "axe");

        File fileGeneral = new File(recipes, "/general.json");
        if (fileGeneral.exists())
            GeneralParser.parseFile(fileGeneral);
    }


    private static void loadJson(File recipes, String type, String subDir) {
        File[] dir = new File(recipes, type + "/" + subDir).listFiles();
        if (dir != null)
            for (File file : dir) {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                if (extension.equalsIgnoreCase("json")) {
                    RecipeParser.parseFile(file, type, subDir);
                }
            }
    }
}
