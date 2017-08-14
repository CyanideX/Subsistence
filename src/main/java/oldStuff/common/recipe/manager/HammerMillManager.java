package oldStuff.common.recipe.manager;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import oldStuff.common.util.ItemHelper;

import java.util.List;

/**
 * @author dmillerw
 */
public class HammerMillManager {

    // Kinda pointless, but makes for cleaner code
    private static class Wrapper {
        public ItemStack[] data;
        public Wrapper(ItemStack[] data) {
            this.data = data;
        }
    }

    private static List<Wrapper> recipes = Lists.newArrayList();

    public static void addData(ItemStack[] data) {
        recipes.add(new Wrapper(data));
    }

    public static ItemStack getOutput(ItemStack input, int stage) {
        for (Wrapper wrapper : recipes) {
            for (int i=0; i<wrapper.data.length; i++) {
                ItemStack stack = wrapper.data[i];
                if (ItemHelper.areItemsEqual(input, stack)) {
                    if (i < 5 && ((stage + 1) > i)) {
                        ItemStack out = wrapper.data[stage + 1];
                        return out != null ? out.copy() : null;
                    }
                }
            }
        }
        return null;
    }
}
