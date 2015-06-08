package subsistence.common.recipe.wrapper.stack;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GenericItem {

    public static GenericItem merge(GenericItem... array) {
        List<ItemStack> list = Lists.newArrayList();
        for (GenericItem genericItem : array) {
            if (genericItem == null)
                continue;

            for (ItemStack itemStack : genericItem.contents) {
                list.add(itemStack);
            }
        }

        GenericItem genericItem = new GenericItem();
        genericItem.contents = list.toArray(new ItemStack[list.size()]);
        return genericItem;
    }

    public ItemStack[] contents;

    public boolean valid() {
        if (contents == null || contents.length == 0) {
            return false;
        } else {
            for (ItemStack s : contents) {
                if (s == null || s.getItem() == null) {
                    return false;
                }
            }
        }
        return true;
    }
}
