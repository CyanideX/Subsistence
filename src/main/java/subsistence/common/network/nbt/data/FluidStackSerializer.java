package subsistence.common.network.nbt.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackSerializer extends AbstractSerializer<FluidStack> {

    @Override
    public boolean canHandle(Class<?> fieldType) {
        return fieldType == FluidStack.class;
    }

    @Override
    public void serialize(String name, Object object, NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        ((FluidStack) object).writeToNBT(tag);
        nbt.setTag(name, tag);
    }

    @Override
    public FluidStack deserialize(String name, NBTTagCompound nbt) {
        return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(name));
    }
}
