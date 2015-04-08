package com.subsistence.common.lib.stack;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author dmillerw
 */
public class FixedItemStack {

    public static ItemStack loadFromNBT(NBTTagCompound nbt) {
        Item item = GameData.getItemRegistry().getObject(nbt.getString("Item"));
        byte count = nbt.getByte("Count");
        short damage = nbt.getShort("Damage");
        NBTTagCompound tag = nbt.getCompoundTag("tag");

        if (item == null) {
            return null;
        }

        ItemStack stack = new ItemStack(item, count, damage);
        if (tag != null) {
            stack.setTagCompound(tag);
        }

        return stack;
    }

    public static void writeToNBT(ItemStack stack, NBTTagCompound nbt) {
        nbt.setString("Item", GameData.getItemRegistry().getNameForObject(stack.getItem()));
        nbt.setByte("Count", (byte) stack.stackSize);
        nbt.setShort("Damage", (short) stack.getItemDamage());
        if (stack.stackTagCompound != null) {
            nbt.setTag("tag", stack.stackTagCompound);
        }
    }
}
