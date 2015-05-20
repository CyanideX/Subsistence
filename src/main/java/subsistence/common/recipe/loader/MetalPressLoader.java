package subsistence.common.recipe.loader;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.MetalPressRecipe;
import subsistence.common.recipe.wrapper.stack.GenericStack;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class MetalPressLoader {

    public static class Recipe {

        public GenericStack input;
        public ItemStack output;
        public int amount = 1;
    }

    public static void parseFile(File file) {
        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            Recipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), Recipe[].class);

            for (Recipe recipe : recipes) {
                verifyParse(file.getName(), recipe);
            }
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            ex.printStackTrace();
        } catch (JsonSyntaxException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            ex.printStackTrace();
        }
    }

    private static void verifyParse(String name, Recipe recipe) {
        for (ItemStack input : recipe.input.contents) {
            SubsistenceRecipes.METAL_PRESS.register(new MetalPressRecipe(input, recipe.output, recipe.amount));
        }
    }
}
