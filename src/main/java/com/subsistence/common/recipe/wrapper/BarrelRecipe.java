package com.subsistence.common.recipe.wrapper;

import com.subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

/**
 * @author dmillerw
 */
public class BarrelRecipe {

    public final Fluid fluid;

    public final ItemStack input;
    public final ItemStack output;

    public BarrelRecipe(Fluid fluid, ItemStack input, ItemStack output) {
        this.fluid = fluid;
        this.input = input;
        this.output = output;
    }

    public boolean valid(Fluid fluid, ItemStack stack) {
        return this.fluid.getID() == fluid.getID() && StackHelper.areStacksSimilar(input, stack, true);
    }

    public ItemStack get(ItemStack stack) {
        return output.copy();
    }
}
