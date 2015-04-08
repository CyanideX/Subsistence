package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.IGenericRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class GenericRecipeManager<T extends IGenericRecipe> {

    private List<T> recipes = new ArrayList<T>();

    public void register(T recipe) {
        recipes.add(recipe);
    }

    public T get(ItemStack stack, boolean block) {
        for (IGenericRecipe recipe : recipes) {
            if (recipe.valid(stack)) {
                return (T) recipe;
            }
        }
        return null;
    }
}
