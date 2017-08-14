package oldStuff.common.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class StackHelper {

    public static ItemStack[] convert(Object object) {
        if (object instanceof ItemStack[]) {
            return (ItemStack[]) object;
        }

        if (object instanceof ItemStack) {
            ItemStack stack = (ItemStack) object;
            if (stack.getItemDamage() < 0) {
                stack.setItemDamage(0);
            }
            return new ItemStack[]{stack};
        }

        if (object instanceof Block) {
            return new ItemStack[]{new ItemStack((Block) object)};
        }

        if (object instanceof Item) {
            return new ItemStack[]{new ItemStack((Item) object)};
        }

        if (object instanceof String) {
            List<ItemStack> ores = OreDictionary.getOres(object.toString());
            return ores.toArray(new ItemStack[ores.size()]);
        }

        return null;
    }

    public static boolean areStacksSimilar(ItemStack stack1, ItemStack stack2, boolean ignoreNBT) {
        if (stack1.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack2.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return stack1.getItem() == stack2.getItem() && (ignoreNBT || ItemStack.areItemStackTagsEqual(stack1, stack2));
        } else {
            return ((stack1.getItem() == stack2.getItem()) && stack1.getItemDamage() == stack2.getItemDamage()) && (ignoreNBT || ItemStack.areItemStacksEqual(stack1, stack2));
        }
    }

    public static ItemStack copyAndResize(ItemStack ingot, int size) {
        ItemStack copy = ingot.copy();
        copy.stackSize = size;
        return copy;
    }
}
