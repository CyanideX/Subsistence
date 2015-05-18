package subsistence.common.config;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class TableConfig {

    public static class Recipe {

        public ItemStack input;
        public ItemStack output;
        public float durability;
        public int duration;
        public boolean perishable;
        public String type = "both";
    }


    public static void parseFile(File file, String type) {
        try {
            SubsistenceLogger.info("Parsing " + file.getName());
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

    public static void makeNewFiles() {
        //TODO: make new files
    }

    public static void verifyParse(String name, Recipe recipe, String type) {
        boolean hammerMill = recipe.type.equalsIgnoreCase("mill") || recipe.type.equalsIgnoreCase("both");
        boolean table = recipe.type.equalsIgnoreCase("table") || recipe.type.equalsIgnoreCase("both");

        if (!table && !hammerMill) {
            throw new NullPointerException("Please specify table or mill");
        }

        if (type.equals("hammer"))
            SubsistenceRecipes.TABLE.registerHammerRecipe(recipe.input, recipe.output, recipe.durability, recipe.duration, table, hammerMill);
        else if (type.equals("drying")) {
            SubsistenceRecipes.TABLE.registerDryingRecipe(recipe.input, recipe.output, recipe.duration);
            if (recipe.perishable) {
                SubsistenceRecipes.PERISHABLE.put(recipe.output.getItem(), recipe.duration);
            }
        } else if (type.equals("axe"))
            SubsistenceRecipes.TABLE.registerRecipe(recipe.input, recipe.output, ToolDefinition.AXE, recipe.durability, recipe.duration, table, hammerMill);
    }
}
