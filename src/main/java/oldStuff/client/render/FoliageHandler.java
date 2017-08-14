package oldStuff.client.render;

import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import oldStuff.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @author dmillerw
 */
public class FoliageHandler {

    private static Set<ItemStack> foliage = Sets.newHashSet();

    public static void initialize(File file) {
        // Fill with defaults, just in case
        foliage.add(new ItemStack(Blocks.leaves, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.sapling, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.red_flower, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.yellow_flower, 1, OreDictionary.WILDCARD_VALUE));
        foliage.add(new ItemStack(Blocks.vine, 1));
        foliage.add(new ItemStack(Blocks.waterlily, 1));

        if (file != null) {
            try {
                ItemStack[] array = JsonUtil.gson().fromJson(new FileReader(file), ItemStack[].class);
                Collections.addAll(foliage, array);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean shouldRender(ItemStack itemStack) {
        for (ItemStack stack : foliage) {
            // More specific detection?
            if (stack.getItem() == itemStack.getItem()) {
                return true;
            }
        }
        return false;
    }
}
