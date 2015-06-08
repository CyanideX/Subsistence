package subsistence.common.recipe.loader;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.RandomStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.SieveRecipe;
import subsistence.common.recipe.wrapper.stack.GenericItem;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SieveLoader {

    public static class Recipe {

        public GenericItem input;
        public Output[] output;
        public Duration duration = new Duration(20, 20);
        public String type = "both";

        public boolean valid() {
            if (!input.valid()) {
                return false;
            }
            for (Output o : output) {
                if (o == null || o.item == null || o.item.getItem() == null) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Output {

        public ItemStack item;
        public float chance = 0F;
    }

    public static class Duration {

        public Duration() {
        }

        public Duration(int b, int h) {
            block = b;
            hand = h;
        }

        public int block;
        public int hand;
    }

    public static void parseFile(File file) {
        try {
            SubsistenceLogger.info("Parsing sieve recipe: " + file.getName());
            Recipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), Recipe[].class);

            for (Recipe recipe : recipes) {
                verifyParse(file.getName(), recipe);
            }
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
        }
    }

    public static void verifyParse(String name, Recipe recipe) {
        List<RandomStack> list = Lists.newArrayList();

        if (recipe.valid()) {

            if (recipe.output != null) {
                for (Output output : recipe.output) {
                    list.add(new RandomStack(output.item, output.chance));
                }
            }

            final RandomStack[] array = list.toArray(new RandomStack[list.size()]);

            for (ItemStack stack : recipe.input.contents) {
                SubsistenceRecipes.SIEVE.register(new SieveRecipe(stack, array, recipe.duration.block, recipe.duration.hand, recipe.type));
            }
        }
    }
}