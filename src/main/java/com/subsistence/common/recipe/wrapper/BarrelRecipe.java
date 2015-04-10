package com.subsistence.common.recipe.wrapper;

import com.subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author dmillerw
 */
public class BarrelRecipe {

    private final Fluid fluid;
    private final int fluidAmount;
    private final ItemStack input;
    private final ItemStack output;
    private final boolean wood;

    public BarrelRecipe(Fluid fluid, int fluidAmount, ItemStack input, ItemStack output, boolean wood) {
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.input = input;
        this.output = output;
        this.wood = wood;
    }

    public boolean valid(FluidStack fluidStack, ItemStack stack, boolean wood) {
        return this.fluid.getID() == fluidStack.fluidID && this.fluidAmount == fluidStack.amount && this.wood == wood && StackHelper.areStacksSimilar(input, stack, true);
    }

    public ItemStack get() {
        return output.copy();
    }
}
