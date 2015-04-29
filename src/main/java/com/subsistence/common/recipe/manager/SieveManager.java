package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.CompostRecipe;
import com.subsistence.common.recipe.wrapper.SieveRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lclc98
 */
public class SieveManager {
    private List<SieveRecipe> recipes = new ArrayList<SieveRecipe>();

    public SieveRecipe get(ItemStack stack) {
        for (SieveRecipe recipe : recipes) {
            if (recipe.valid(stack)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(SieveRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }


}
