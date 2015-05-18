package subsistence.common.config;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.core.RecipeParser;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import subsistence.common.util.JsonUtil;
import subsistence.common.util.StackHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author lclc98
 */
public class BarrelConfig {

    public static class ParsedRecipe {

        public Recipe[] recipes;
    }

    public static class Recipe {

        public String[] inputItem;
        public String inputLiquid;
        public boolean typeWood = false;
        public Output output;
    }

    public static class Output {

        public String outputLiquid;

        public int timeTorch;
        public int timeLava;
        public int timeFire;

        public String outputItem;
        public int time;
    }

    public static void parseFile(File file) {
        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            ParsedRecipe recipe = JsonUtil.gson().fromJson(new FileReader(file), ParsedRecipe.class);
            verifyParse(file.getName(), recipe);
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
        }
    }

    public static void makeNewFiles() {
        //TODO: default file
    }

    private static void verifyParse(String name, ParsedRecipe recipe) {
        for (Recipe recipe1 : recipe.recipes) {
            ArrayList<ItemStack> inputItem = new ArrayList<ItemStack>();
            FluidStack inputLiquid = null;
            if (recipe1.inputLiquid != null)
                inputLiquid = RecipeParser.getLiquid(recipe1.inputLiquid);

            ItemStack outputItem = null;
            FluidStack outputLiquid = null;

            if (recipe1.inputItem.length > 0) {
                for (String inputList : recipe1.inputItem) {
                    ItemStack[] tempInput = StackHelper.convert(RecipeParser.getItem(inputList));
                    for (ItemStack stack : tempInput)
                        inputItem.add(stack);
                }
            }

            if (recipe1.output.outputItem != null && !recipe1.output.outputItem.isEmpty()) {
                outputItem = StackHelper.convert(RecipeParser.getItem(recipe1.output.outputItem))[0];
            }
            if (recipe1.output.outputLiquid != null && !recipe1.output.outputLiquid.isEmpty()) {
                outputLiquid = RecipeParser.getLiquid(recipe1.output.outputLiquid);
            }

            if (inputItem.size() <= 0 && inputLiquid == null) {
                throw new NullPointerException("Inputs is null!");
            }
            if (outputItem == null && outputLiquid == null) {
                throw new NullPointerException("Outputs can't be null!");
            }


            if (recipe1.typeWood)
                SubsistenceRecipes.BARREL.registerWood(new BarrelWoodRecipe(inputItem.toArray(new ItemStack[inputItem.size()]), inputLiquid, outputItem, outputLiquid, recipe1.output.time));
            else
                SubsistenceRecipes.BARREL.registerStone(new BarrelStoneRecipe(inputItem.toArray(new ItemStack[inputItem.size()]), inputLiquid, outputItem, outputLiquid, recipe1.output.time, recipe1.output.timeTorch, recipe1.output.timeLava, recipe1.output.timeFire));

        }

        int length = recipe.recipes.length;
        SubsistenceLogger.info("Parsed " + name + ". Loaded " + length + (length > 1 ? " recipes" : " recipe"));
    }
}