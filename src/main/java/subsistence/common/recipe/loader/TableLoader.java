package subsistence.common.recipe.loader;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.TableAxeRecipe;
import subsistence.common.recipe.wrapper.TableDryingRecipe;
import subsistence.common.recipe.wrapper.TableSmashingRecipe;
import subsistence.common.recipe.wrapper.stack.GenericItem;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TableLoader {

    public static class Recipe {

        public GenericItem input;
        public ItemStack output;

        public int duration; // Only for drying
        public int durability; // Only for smashing/axe
    }


    public static void parseFile(File file, String type) {
        try {
            SubsistenceLogger.info("Parsing table " + type + " recipe: " + file.getName());
            Recipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), Recipe[].class);

            for (Recipe recipe : recipes) {
                verifyParse(file.getName(), recipe, type);
            }
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            ex.printStackTrace();
        } catch (JsonSyntaxException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            ex.printStackTrace();
        }
    }

    public static void verifyParse(String name, Recipe recipe, String type) {
        if (!recipe.input.valid() || recipe.output == null || recipe.output.getItem() == null) {
            return;
        }

        if ("smash".equals(type)) {
            for (ItemStack itemStack : recipe.input.contents) {
                SubsistenceRecipes.TABLE.register(new TableSmashingRecipe(itemStack, recipe.output, recipe.durability));
            }
        } else if ("dry".equals(type)) {
            for (ItemStack itemStack : recipe.input.contents) {
                SubsistenceRecipes.TABLE.register(new TableDryingRecipe(itemStack, recipe.output, recipe.duration));
            }
        } else if ("axe".equals(type)) {
            for (ItemStack itemStack : recipe.input.contents) {
                SubsistenceRecipes.TABLE.register(new TableAxeRecipe(itemStack, recipe.output, recipe.durability));
            }
        }
    }
}
