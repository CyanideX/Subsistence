package com.subsistence.common.recipe.wrapper;

import com.subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;

/**
 * @author lclc98
 */
public class TableDryingRecipe {


    private final ItemStack input;
    private final ItemStack output;
    private final float speed;

    public TableDryingRecipe(ItemStack input, ItemStack output, float speed) {
        this.input = input;
        this.output = output;
        this.speed = speed;
    }

    public boolean isInputStack(ItemStack stack) {
        return stack != null && StackHelper.areStacksSimilar(stack, input, true);
    }

    public float getSpeed() {
        return speed;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInputItem() {
        return input;
    }
}