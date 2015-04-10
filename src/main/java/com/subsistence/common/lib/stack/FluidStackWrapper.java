package com.subsistence.common.lib.stack;

import net.minecraftforge.fluids.FluidStack;

/**
 * @author dmillerw
 */
public class FluidStackWrapper extends GenericStackWrapper<FluidStack> {

    public FluidStackWrapper(FluidStack contents) {
        super(contents);
    }

    @Override
    public GenericStackWrapper<FluidStack> copy() {
        return new FluidStackWrapper(contents.copy());
    }

    @Override
    public boolean equals(GenericStackWrapper<FluidStack> wrapper) {
        return !(wrapper.contents == null || wrapper.contents.getFluid() == null) && (wrapper.contents.isFluidEqual(this.contents));

    }

    @Override
    public int hashCode() {
        return contents.fluidID;
    }
}
