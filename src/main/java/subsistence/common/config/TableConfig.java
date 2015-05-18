package subsistence.common.config;

import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.module.RestrictedType;
import subsistence.common.recipe.wrapper.module.component.ComponentItem;
import subsistence.common.recipe.wrapper.module.core.ModularObject;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class TableConfig {

    public static class Recipe {

        @RestrictedType("generic.item")
        public ModularObject input;
        @RestrictedType("generic.item")
        public ModularObject output;
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
        } catch (IOException|JsonSyntaxException ex) { //if multi catches can not be used in your ide, change your project level to 7+
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

        final ItemStack input = ((ComponentItem)recipe.input).itemStack;
        final ItemStack output = ((ComponentItem)recipe.output).itemStack;

        if (type.equals("hammer"))
            SubsistenceRecipes.TABLE.registerHammerRecipe(input, output, recipe.durability, recipe.duration, table, hammerMill);
        else if (type.equals("drying")) {
            SubsistenceRecipes.TABLE.registerDryingRecipe(input, output, recipe.duration);
            if (recipe.perishable) {
                SubsistenceRecipes.PERISHABLE.put(output.getItem(), recipe.duration);
            }
        } else if (type.equals("axe"))
            SubsistenceRecipes.TABLE.registerRecipe(input, output, ToolDefinition.AXE, recipe.durability, recipe.duration, table, hammerMill);
    }
}
