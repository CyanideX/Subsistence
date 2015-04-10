package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.BarrelRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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

    public BarrelRecipe get(FluidStack fluid, ItemStack stack, boolean wood) {
        for (BarrelRecipe recipe : recipes) {
            if (recipe.valid(fluid, stack, wood)) {
                return recipe;
            }
        }
        return null;
    }
}
