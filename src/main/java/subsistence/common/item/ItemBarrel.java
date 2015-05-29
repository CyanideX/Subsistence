package subsistence.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.block.machine.BlockBarrel;
import subsistence.common.core.SubsistenceCreativeTab;

public final class ItemBarrel extends ItemBlock {

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

    public void setInput(ItemStack stack, ItemStack[] inv) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();

        NBTTagList items = new NBTTagList();
        for (int i = 0; i < inv.length; i++) {
            if (inv[i] != null) {
                NBTTagCompound c = new NBTTagCompound();
                c.setByte("Slot", (byte) i);
                inv[i].writeToNBT(c);
                items.appendTag(c);
            }
        }
        nbt.setTag("Items", items);
    }

    public void setFluid(ItemStack stack, FluidStack fluid) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();
        NBTTagCompound fluidTag = new NBTTagCompound();
        fluid.writeToNBT(fluidTag);
        nbt.setTag("Fluid", fluidTag);
    }

    public ItemStack[] getInput(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();
        ItemStack[] inv = new ItemStack[64];
        NBTTagList items = nbt.getTagList("Items", 10);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound c = items.getCompoundTagAt(i);
            inv[c.getByte("Slot")] = ItemStack.loadItemStackFromNBT(c);
        }
        return inv;
    }

    public FluidStack getFluid(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbt = stack.getTagCompound();
        return FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("Fluid"));
    }

    public void setLid(ItemStack stack, boolean hasLid) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbt = stack.getTagCompound();
        nbt.setBoolean("hasLid", hasLid);
    }

    public boolean hasLid(ItemStack stack) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbt = stack.getTagCompound();
        return nbt.getBoolean("hasLid");
    }
}