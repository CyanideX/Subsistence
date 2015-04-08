package com.subsistence.common.item;

import com.subsistence.common.block.machine.BlockBarrel;
import com.subsistence.common.core.SubsistenceCreativeTab;
import com.subsistence.common.lib.SubsistenceTag;
import com.subsistence.common.lib.IBarrel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

public final class ItemBarrel extends ItemBlock implements IBarrel {
    public ItemBarrel(Block b) {
        super(b);
        this.setCreativeTab(SubsistenceCreativeTab.BLOCKS.get());
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + ((BlockBarrel) this.field_150939_a).getNameForType(stack.getItemDamage());
    }

    @Override
    public void setInput(ItemStack stack, ItemStack[] inv) {
        NBTTagCompound comp = SubsistenceTag.get(stack);
        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                NBTTagCompound c = new NBTTagCompound();
                c.setByte("Slot", (byte) i);
                inv[i].writeToNBT(c);
                items.appendTag(c);
            }
        }
        comp.setTag("Items", items);
    }

    @Override
    public void setFluid(ItemStack stack, FluidStack fluid) {
        NBTTagCompound comp = SubsistenceTag.get(stack);
        NBTTagCompound fluidTag = new NBTTagCompound();
        fluid.writeToNBT(fluidTag);
        comp.setTag("Fluid", fluidTag);
    }

    @Override
    public ItemStack[] getInput(ItemStack stack) {
        NBTTagCompound comp = SubsistenceTag.get(stack);
        ItemStack[] inv = new ItemStack[2];
        NBTTagList items = comp.getTagList("Items", 10);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound c = items.getCompoundTagAt(i);
            inv[c.getByte("Slot")] = ItemStack.loadItemStackFromNBT(c);
        }
        return inv;
    }

    @Override
    public FluidStack getFluid(ItemStack stack) {
        NBTTagCompound comp = SubsistenceTag.get(stack);
        return FluidStack.loadFluidStackFromNBT(comp.getCompoundTag("Fluid"));
    }
}