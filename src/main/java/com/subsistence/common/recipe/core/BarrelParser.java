package com.subsistence.common.recipe.core;

import com.google.gson.Gson;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.wrapper.BarrelRecipe;
import com.subsistence.common.tile.machine.TileBarrel;
import com.subsistence.common.util.StackHelper;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class BarrelParser {

    public static class ParsedSieveRecipe {

        public boolean crash_on_fail = true;
        public int rain;
        public Recipe[] recipes;


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("CRASH_ON_FAIL: ").append(crash_on_fail);
            sb.append("RECIPES: [");
            for (int i = 0; i < recipes.length; i++) {
                Recipe output = recipes[i];
                sb.append(output.toString());
                if (i != recipes.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public static class Recipe {

        public int fluidAmount;
        public String input;
        public String output;
        public boolean wood;
    }


    public static void parseFile(File file) {
        try {
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            ParsedSieveRecipe recipe = new Gson().fromJson(new FileReader(file), ParsedSieveRecipe.class);
            verifyParse(file.getName(), recipe);
        } catch (IOException ex) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
        }
    }

    private static void verifyParse(String name, ParsedSieveRecipe recipe) {
        for (Recipe recipe1 : recipe.recipes) {
            ItemStack[] input = StackHelper.convert(RecipeParser.getItem(recipe1.input));
            ItemStack[] output = StackHelper.convert(RecipeParser.getItem(recipe1.output));
            if (recipe.crash_on_fail) {
                if (input == null) {
                    throw new NullPointerException(recipe1.input + " is not a valid item!");
                }
                if (output == null) {
                    throw new NullPointerException(recipe1.output + " is not a valid item!");
                }
            }
            for (ItemStack stack : input)
                for (ItemStack stackOutput : output)
                    SubsistenceRecipes.BARREL.register(new BarrelRecipe(null, recipe1.fluidAmount, stack, stackOutput, recipe1.wood));
            TileBarrel.rain = recipe.rain;
        }

        int length = recipe.recipes.length;
        FMLLog.info("[Subsistence] Parsed " + name + ". Loaded " + length + (length > 1 ? " recipes" : " recipe"));
    }
}