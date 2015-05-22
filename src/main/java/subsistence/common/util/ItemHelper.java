package subsistence.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;
import java.util.Map;

public class ItemHelper {

    public static int getID(ItemStack stack) {
        return getID(stack.getItem());
    }

    public static int getID(Block block) {
        return GameData.getBlockRegistry().getId(block);
    }

    public static int getID(Item item) {
        return GameData.getItemRegistry().getId(item);
    }

    public static boolean isBlock(ItemStack stack, Block block) {
        return getID(stack) == getID(block);
    }

    public static boolean areItemsEqual(ItemStack i1, ItemStack i2) {
        if (i1 == null || i2 == null)
            return false;

        if (i1.getItem() != i2.getItem())
            return false;

        if (i1.getItemDamage() == OreDictionary.WILDCARD_VALUE || i2.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return true;
        } else {
            return i1.getItemDamage() == i2.getItemDamage();
        }
    }

    public static ItemStack sanitizeStack(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        if (copy.hasTagCompound() && copy.getTagCompound().hasNoTags()) {
            copy.setTagCompound(null);
        }
        return copy;
    }

    public static List<ItemStack> mergeLikeItems(List<ItemStack> list) {
        // We then merge all the similar stacks together
        Map<ItemAndIntTuple, List<ItemStack>> similarItems = Maps.newHashMap();

        for (ItemStack stack : list) {
            ItemAndIntTuple tuple = new ItemAndIntTuple(stack.getItem(), stack.getItemDamage());

            List<ItemStack> newList = similarItems.get(tuple);
            if (newList == null) {
                newList = Lists.newArrayList();
            }

            newList.add(stack);

            similarItems.put(tuple, newList);
        }

        List<ItemStack> finalList = Lists.newArrayList();

        // Then, for each grouped item, we grab the first stack, and add the stack sizes of the rest to the first
        for (Map.Entry<ItemAndIntTuple, List<ItemStack>> entry : similarItems.entrySet()) {
            List<ItemStack> itemStacks = entry.getValue();
            ItemStack first = entry.getValue().get(0);

            if (itemStacks.size() > 1) {
                for (int i = 1; i < itemStacks.size(); i++) {
                    first.stackSize += itemStacks.get(i).stackSize;
                }
            }

            finalList.add(first);
        }

        return finalList;
    }

    public static class ItemAndIntTuple {

        public Item item;
        public int damage;

        public ItemAndIntTuple(Item item, int damage) {
            this.item = item;
            this.damage = damage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemAndIntTuple that = (ItemAndIntTuple) o;

            if (damage != that.damage) return false;
            return item.equals(that.item);

        }

        @Override
        public int hashCode() {
            int result = item.hashCode();
            result = 31 * result + damage;
            return result;
        }
    }
}
