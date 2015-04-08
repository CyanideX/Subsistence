package com.subsistence.common.recipe;

import com.subsistence.common.block.BlockStorage;
import com.subsistence.common.item.SubsistenceItems;
import com.subsistence.common.item.resource.ItemResource;
import com.subsistence.common.recipe.wrapper.SieveRecipe;
import com.subsistence.common.block.SubsistenceBlocks;
import com.subsistence.common.recipe.manager.BarrelManager;
import com.subsistence.common.recipe.manager.GenericRecipeManager;
import com.subsistence.common.recipe.manager.TableManager;
import com.subsistence.common.util.StackHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * @author dmillerw
 */
public class SubsistenceRecipes {

    public static TableManager TABLE = new TableManager();

    public static GenericRecipeManager<SieveRecipe> SIEVE = new GenericRecipeManager<SieveRecipe>();

    public static BarrelManager BARREL = new BarrelManager();
    public static HashMap<Item, Integer> PERISHABLE = new HashMap<Item, Integer>();

    public static void initialize() {
        addCraftingRecipes();
    }

    private static void addCraftingRecipes() {
        for (int i = 0; i < ItemResource.NAMES.length; i++) {
            ItemStack ingot = new ItemStack(SubsistenceItems.resourceIngot, 1, i);
            ItemStack storage = BlockStorage.getStorageFromResource(ingot);

            if (storage != null) {
                GameRegistry.addShapedRecipe(storage, "III", "III", "III", 'I', ingot);
                GameRegistry.addShapelessRecipe(StackHelper.copyAndResize(ingot, 9), storage);
            }
        }

        //Tools
        GameRegistry.addShapedRecipe(new ItemStack(SubsistenceItems.component, 1, 1), "II", "II", 'I', new ItemStack(SubsistenceItems.component, 1, 0));

        //Machines
        GameRegistry.addShapedRecipe(new ItemStack(SubsistenceBlocks.tableSieve), "IXI", "SXS", "WWW", 'I', Blocks.wooden_slab, 'X', new ItemStack(SubsistenceItems.component, 1, 1), 'S', new ItemStack(Items.stick), 'W', Blocks.planks);
    }
}
