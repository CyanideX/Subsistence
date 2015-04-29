package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.CompostRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lclc98
 */
public class CompostManager {
    private List<CompostRecipe> recipes = new ArrayList<CompostRecipe>();

    public CompostRecipe get(FluidStack fluid, ItemStack[] stack) {
        for (CompostRecipe recipe : recipes) {
            if (recipe.valid(fluid, stack)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(CompostRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }

}
