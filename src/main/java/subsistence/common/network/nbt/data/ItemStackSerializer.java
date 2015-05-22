package subsistence.common.network.nbt.data;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import subsistence.common.lib.stack.FixedItemStack;

public class ItemStackSerializer extends AbstractSerializer<ItemStack> {

    @Override
    public boolean canHandle(Class<?> fieldType) {
        return fieldType == ItemStack.class;
    }

    @Override
    public void serialize(String name, Object object, NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        FixedItemStack.writeToNBT((ItemStack) object, tag);
        nbt.setTag(name, tag);
    }

    @Override
    public ItemStack deserialize(String name, NBTTagCompound nbt) {
        return FixedItemStack.loadFromNBT(nbt.getCompoundTag(name));
    }
}
