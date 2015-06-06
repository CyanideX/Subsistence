package subsistence.common.recipe.loader;

import net.minecraft.item.ItemStack;
import subsistence.common.recipe.manager.HammerMillManager;
import subsistence.common.util.JsonUtil;

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
        for (ItemStack[] recipe : data) {
            if (recipe.length != 5) {
                // Error
                continue;
            }

            HammerMillManager.addData(recipe);
        }
    }
}
