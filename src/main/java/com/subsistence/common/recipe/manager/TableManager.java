package com.subsistence.common.recipe.manager;

import com.subsistence.common.lib.DurabilityMapping;
import com.subsistence.common.lib.tool.ToolDefinition;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import com.subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import com.subsistence.common.recipe.wrapper.TableDryingRecipe;
import com.subsistence.common.recipe.wrapper.TableRecipe;
import com.subsistence.common.util.StackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class TableManager {

    private List<TableRecipe> recipesTable = new ArrayList<TableRecipe>();
    private List<TableDryingRecipe> recipesDrying = new ArrayList<TableDryingRecipe>();

    public void registerHammerRecipe(Object input, Object output, float durability, float speed, boolean table, boolean hammerMill) {
        registerRecipe(input, output, ToolDefinition.HAMMER, durability, speed, table, hammerMill);
    }

    public void registerRecipe(Object input, Object output, ToolDefinition tool, float durability, float speed, boolean table, boolean hammerMill) {
        if (input == null || output == null || tool == null || durability < 0F) {
            return;
        }

        ItemStack[] in = StackHelper.convert(input);
        ItemStack[] out = StackHelper.convert(output);

        if (out.length > 0) {
            for (ItemStack stack : in) {
                DurabilityMapping.INSTANCE.registerDurablity(stack, durability);
                register(new TableRecipe(stack, out[0], tool, speed, table, hammerMill, true, true));
            }
        }
    }

    public void register(TableRecipe recipe) {
        recipesTable.add(recipe);
    }

    public TableRecipe get(ItemStack input, ItemStack tool, boolean isTable) {
        for (TableRecipe recipe : recipesTable) {
            if (recipe.isInput(input, tool, isTable)) {
                return recipe;
            }
        }
        return null;
    }

    public TableRecipe get(ItemStack input, ToolDefinition tool, boolean isTable) {
        for (TableRecipe recipe : recipesTable) {
            if (recipe.isInput(input, tool, isTable)) {
                return recipe;
            }
        }
        return null;
    }


    public void registerDryingRecipe(Object input, Object output, float speed) {
        if (input == null || output == null || speed < 0F) {
            return;
        }

        ItemStack[] in = StackHelper.convert(input);
        ItemStack[] out = StackHelper.convert(output);

        if (out.length > 0) {
            for (ItemStack stack : in) {
                register(new TableDryingRecipe(stack, out[0], speed));
            }
        }
    }

    public void register(TableDryingRecipe recipe) {
        recipesDrying.add(recipe);
    }

    public TableDryingRecipe getDrying(ItemStack input) {
        for (TableDryingRecipe recipe : recipesDrying) {
            if (recipe.isInputStack(input)) {
                return recipe;
            }
        }
        return null;
    }

    public void clear() {
        recipesTable.clear();
        recipesDrying.clear();
    }

}
