package subsistence.common.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
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

    public static ItemStack sanitizeStack(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        if (copy.hasTagCompound() && copy.getTagCompound().hasNoTags()) {
            copy.setTagCompound(null);
        }
        return copy;
    }
}
