package subsistence.common.recipe.loader;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import subsistence.common.recipe.wrapper.stack.GenericItem;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BarrelLoader {

    // Wood recipes are immediate
    public static class WoodRecipe {

        /* INPUTS */
        public Input input;
        public Output output;

        public String conditional = "all"; // all/any/any_with_global_limit

        @SerializedName("global_limit")
        public int globalLimit;
    }

    // Stone recipes have a set time via heat
    public static class StoneRecipe {

        /* INPUTS */
        public Input input;
        public Output output;

        public Heat heat;

        public String conditional = "all"; // all/any/any_with_global_limit

        @SerializedName("global_limit")
        public int globalLimit;
    }

    private static class Input {

        public GenericItem item;
        public FluidStack fluid;
    }

    private static class Output {

        public ItemStack item;
        public FluidStack fluid;
    }

    private static class Heat {

        public int torch = -1;
        public int fire = -1;
        public int lava = -1;
    }

    public static void parseFile(File file, String type) {
        try {
            if ("wood".equals(type)) {
                WoodRecipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), WoodRecipe[].class);
                for (WoodRecipe recipe : recipes) {
                    verifyWood(recipe);
                }
            } else if ("stone".equals(type)) {
                StoneRecipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), StoneRecipe[].class);
                for (StoneRecipe recipe : recipes) {
                    verifyStone(recipe);
                }
            }
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
        }
    }

    private static void verifyWood(WoodRecipe woodRecipe) {
        SubsistenceRecipes.BARREL.registerWood(new BarrelWoodRecipe(
                GenericItem.merge(woodRecipe.input.item).contents,
                woodRecipe.input.fluid,
                woodRecipe.output.item,
                woodRecipe.output.fluid,
                woodRecipe.conditional,
                woodRecipe.globalLimit
        ));
    }

    private static void verifyStone(StoneRecipe stoneRecipe) {
        SubsistenceRecipes.BARREL.registerStone(new BarrelStoneRecipe(
                GenericItem.merge(stoneRecipe.input.item).contents,
                stoneRecipe.input.fluid,
                stoneRecipe.output.item,
                stoneRecipe.output.fluid,
                stoneRecipe.conditional,
                stoneRecipe.globalLimit,
                stoneRecipe.heat.torch,
                stoneRecipe.heat.fire,
                stoneRecipe.heat.lava
        ));
    }
}