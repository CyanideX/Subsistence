package oldStuff.common.recipe.loader;

import net.minecraft.item.ItemStack;
import oldStuff.common.recipe.manager.HammerMillManager;
import oldStuff.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author dmillerw
 */
public class HammerMillLoader {

    public static void parseFile(File file) {
        try {
            ItemStack[][] data = JsonUtil.gson().fromJson(new FileReader(file), ItemStack[][].class);
            verifyParse(data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void verifyParse(ItemStack[][] data) {
        loop: for (ItemStack[] recipe : data) {
            if (recipe.length != 5) {
                // Error
                continue;
            }

            for (ItemStack stack : recipe) {
                if (stack == null || stack.getItem() == null) {
                    continue loop;
                }
                stack.stackSize = 1; // Force stack size
            }

            HammerMillManager.addData(recipe);
        }
    }
}
