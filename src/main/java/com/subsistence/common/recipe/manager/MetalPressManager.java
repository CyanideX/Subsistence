package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import com.subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import com.subsistence.common.recipe.wrapper.MetalPressRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lclc98
 */
public class MetalPressManager {
    private List<MetalPressRecipe> recipes = new ArrayList<MetalPressRecipe>();

    public MetalPressRecipe get(ItemStack stack) {
        for (MetalPressRecipe recipe : recipes) {
            if (recipe.valid(stack)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(MetalPressRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }

    public void clear() {
        recipes.clear();
    }

    public boolean isAllowed(ItemStack item) {
        for (MetalPressRecipe recipe : recipes) {
            if (item.getItem() == recipe.getInputItem().getItem()) {
                return true;
            }
        }
        return false;
    }
}
