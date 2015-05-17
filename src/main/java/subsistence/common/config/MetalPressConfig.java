package subsistence.common.config;

import com.google.gson.Gson;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.core.RecipeParser;
import subsistence.common.recipe.wrapper.MetalPressRecipe;
import subsistence.common.util.StackHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class MetalPressConfig {

    public static class ParsedRecipe {
        public Recipe[] recipes;
    }

    public static class Recipe {
        public String inputItem;
        public String outputItem;
        public int amount = 1;
    }

    public static void parseFile(File file) {
        try {
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            ParsedRecipe recipe = new Gson().fromJson(new FileReader(file), ParsedRecipe.class);
            verifyParse(file.getName(), recipe);
        } catch (IOException ex) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
        }
    }

    public static void makeNewFiles () {
        //TODO: make default recipes
    }

    private static void verifyParse(String name, ParsedRecipe recipe) {
        for (Recipe recipe1 : recipe.recipes) {
            ItemStack inputItem = StackHelper.convert(RecipeParser.getItem(recipe1.inputItem))[0];
            ItemStack outputItem = StackHelper.convert(RecipeParser.getItem(recipe1.outputItem))[0];
            if (inputItem == null) {
                throw new NullPointerException("Inputs can't be null!");
            }
            if (outputItem == null) {
                throw new NullPointerException("Outputs can't be null!");
            }


            SubsistenceRecipes.METAL_PRESS.register(new MetalPressRecipe(inputItem, outputItem, recipe1.amount));
        }

        int length = recipe.recipes.length;
        FMLLog.info("[Subsistence] Parsed " + name + ". Loaded " + length + (length > 1 ? " recipes" : " recipe"));
    }
}
