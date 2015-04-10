package com.subsistence.common.recipe.core;

import com.subsistence.common.lib.RandomStack;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.wrapper.SieveRecipe;
import com.subsistence.common.util.StackHelper;
import com.google.gson.Gson;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class SieveParser {

    public static class ParsedSieveRecipe {

        public boolean crash_on_fail = true;
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

        public String input;
        public Output[] outputBlock;
        public Output[] outputHand;
        public int durationBlock;
        public int durationHand;

    }

    public static class Output {

        public String item;
        public float chance;

        @Override
        public String toString() {
            return item + " : " + chance + " : ";
        }
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

    public static void verifyParse(String name, ParsedSieveRecipe recipe) {
        for (Recipe recipe1 : recipe.recipes) {
            ItemStack[] input = StackHelper.convert(RecipeParser.getItem(recipe1.input));

            RandomStack[] outputBlock = new RandomStack[0];
            if (recipe1.outputBlock != null) {
                outputBlock = new RandomStack[recipe1.outputBlock.length];

                // check for ore dictionary
                for (int i = 0; i < recipe1.outputBlock.length; i++) {
                    float chance = recipe1.outputBlock[i].chance;
                    if (chance < 0F) {
                        chance = 0F;
                    } else if (chance > 1F) {
                        chance = 1F;
                    }
                    ItemStack[] stacks = StackHelper.convert(RecipeParser.getItem(recipe1.outputBlock[i].item));
                    if (stacks != null) {
                        for (ItemStack stack : stacks) outputBlock[i] = new RandomStack(stack, chance);
                    }
                }
            }

            RandomStack[] outputHand = new RandomStack[0];
            if (recipe1.outputHand != null) {
                outputHand = new RandomStack[recipe1.outputHand.length];
                for (int i = 0; i < recipe1.outputHand.length; i++) {
                    float chance = recipe1.outputHand[i].chance;
                    if (chance < 0F) {
                        chance = 0F;
                    } else if (chance > 1F) {
                        chance = 1F;
                    }
                    ItemStack[] stacks = StackHelper.convert(RecipeParser.getItem(recipe1.outputHand[i].item));
                    if (stacks != null) {
                        for (ItemStack stack : stacks) outputHand[i] = new RandomStack(stack, chance);
                    }
                }
            }


            if (recipe.crash_on_fail) {
                if (input == null) {
                    throw new NullPointerException(recipe1.input + " is not a valid item!");
                }
            }
            for (ItemStack stack : input)
                SubsistenceRecipes.SIEVE.register(new SieveRecipe(stack, outputBlock, outputHand, recipe1.durationBlock, recipe1.durationHand, true));

        }

        int length = recipe.recipes.length;
        FMLLog.info("[Subsistence] Parsed " + name + ". Loaded " + length + (length > 1 ? " recipes" : " recipe"));
    }
}