package com.subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public interface IGenericRecipe<T> {

    boolean valid(ItemStack stack);

    T get(ItemStack stack, boolean block);
}
