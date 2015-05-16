package subsistence.common.lib;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IBarrel {
    void setInput(ItemStack stack, ItemStack[] inv);

    void setFluid(ItemStack stack, FluidStack fluid);

    ItemStack[] getInput(ItemStack stack);

    FluidStack getFluid(ItemStack stack);

    void setLid(ItemStack stack, boolean hasLid);

    boolean hasLid(ItemStack stack);
}