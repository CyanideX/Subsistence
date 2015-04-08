package com.subsistence.common.lib;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class SubsistenceTag {
    public static final String IDENTIFIER = "Subsistence";

    public static NBTTagCompound get(ItemStack stack) {
        if (stack == null) {
            return null;
        }

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound comp = stack.getTagCompound();
        if (!comp.hasKey(IDENTIFIER)) {
            comp.setTag(IDENTIFIER, new NBTTagCompound());
        }
        return comp.getCompoundTag(IDENTIFIER);
    }
}