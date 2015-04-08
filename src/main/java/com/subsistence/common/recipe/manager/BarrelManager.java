package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.BarrelRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class BarrelManager {

    private List<BarrelRecipe> recipes = new ArrayList<BarrelRecipe>();

    public void register(BarrelRecipe recipe) {
        recipes.add(recipe);
    }

    public BarrelRecipe get(Fluid fluid, ItemStack stack) {
        for (BarrelRecipe recipe : recipes) {
            if (recipe.valid(fluid, stack)) {
                return recipe;
            }
        }
        return null;
    }
}
