package com.subsistence;

import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.core.handler.*;
import com.subsistence.common.fluid.SubsistenceFluids;
import com.subsistence.common.item.SubsistenceItems;
import com.subsistence.common.item.ItemHammer;
import com.subsistence.common.lib.tool.ToolDefinition;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.core.RecipeParser;
import com.subsistence.common.recipe.core.SieveParser;
import com.subsistence.common.recipe.core.TableParser;
import com.subsistence.common.util.EventUtil;
import com.subsistence.common.world.WorldProviderSkyblockHell;
import com.subsistence.common.network.nbt.data.AbstractSerializer;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.DimensionManager;

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

        DimensionManager.unregisterProviderType(-1);
        DimensionManager.unregisterDimension(-1);
        DimensionManager.registerProviderType(-1, WorldProviderSkyblockHell.class, false);
        DimensionManager.registerDimension(-1, -1);

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

    public static void loadJson(){
        File recipes = new File(Subsistence.configPath, "recipes/");
        if (!recipes.exists()) {
            recipes.mkdirs();
        }
        File[] dir = new File(recipes, "sieve").listFiles();
        if (dir != null)
            for (File file : dir) {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                if (extension.equalsIgnoreCase("json")) {
                    SieveParser.parseFile(file);
                }
            }

        loadTableJson(new File(recipes, "table"), "hammer");
        loadTableJson(new File(recipes, "table"), "drying");
        loadTableJson(new File(recipes, "table"), "axe");
    }

    private static void loadTableJson(File recipes, String folder) {
        File[] dir = new File(recipes, folder).listFiles();
        if (dir != null)
            for (File file : dir) {
                String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);

                if (extension.equalsIgnoreCase("json")) {
                    TableParser.parseFile(file, folder);
                }
            }
    }
}
