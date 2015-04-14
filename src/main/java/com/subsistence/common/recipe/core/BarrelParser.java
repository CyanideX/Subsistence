package com.subsistence.common.recipe.core;

import com.google.gson.Gson;
import com.subsistence.common.recipe.SubsistenceRecipes;
import com.subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import com.subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import com.subsistence.common.util.StackHelper;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author lclc98
 */
public class BarrelParser {

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
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            ParsedSieveRecipe recipe = new Gson().fromJson(new FileReader(file), ParsedSieveRecipe.class);
            verifyParse(file.getName(), recipe);
        } catch (IOException ex) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
        }
    }

    private static void verifyParse(String name, ParsedSieveRecipe recipe) {
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

            if (recipe.crash_on_fail) {
                if (inputItem.size() <= 0 && inputLiquid == null)
                    throw new NullPointerException("Inputs is null!");
                if (outputItem == null && outputLiquid == null) {
                    throw new NullPointerException("Outputs can't be null!");
                }
            }


            if (recipe1.typeWood)
                SubsistenceRecipes.BARREL.registerWood(new BarrelWoodRecipe(inputItem.toArray(new ItemStack[inputItem.size()]), inputLiquid, outputItem, outputLiquid, recipe1.output.time));
            else
                SubsistenceRecipes.BARREL.registerStone(new BarrelStoneRecipe(inputItem.toArray(new ItemStack[inputItem.size()]), inputLiquid, outputItem, outputLiquid, recipe1.output.timeTorch, recipe1.output.timeLava, recipe1.output.timeFire));

        }

        int length = recipe.recipes.length;
        FMLLog.info("[Subsistence] Parsed " + name + ". Loaded " + length + (length > 1 ? " recipes" : " recipe"));
    }

    public static <T> T[] concatenate(T[] a, T[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}