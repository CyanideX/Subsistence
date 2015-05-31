package subsistence.common.recipe.loader;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.BarrelMeltingRecipe;
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

        public Heat heat = Heat.BLANK;

        public String conditional = "all"; // all/any/any_with_global_limit

        @SerializedName("global_limit")
        public int globalLimit;
    }

    public static class MeltingRecipe {

        public GenericItem input;
        public FluidStack output;
        public Heat heat;
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

        public static final Heat BLANK = new Heat();

        public int torch = -1;
        public int fire = -1;
        public int lava = -1;
    }

    public static void parseFile(File file, String type) {
        try {
            if ("wood".equals(type)) {
                SubsistenceLogger.info("Parsing wood barrel recipe: " + file.getName());
                WoodRecipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), WoodRecipe[].class);
                for (WoodRecipe recipe : recipes) {
                    verifyWood(recipe);
                }
            } else if ("stone".equals(type)) {
                SubsistenceLogger.info("Parsing stone barrel recipe: " + file.getName());
                StoneRecipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), StoneRecipe[].class);
                for (StoneRecipe recipe : recipes) {
                    verifyStone(recipe);
                }
            } else if ("melting".equals(type)) {
                SubsistenceLogger.info("Parsing stone barrel melting recipe: " + file.getName());
                MeltingRecipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), MeltingRecipe[].class);
                for (MeltingRecipe recipe : recipes) {
                    verifyMelting(recipe);
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

    private static void verifyMelting(MeltingRecipe meltingRecipe) {
        SubsistenceRecipes.BARREL.registerMelting(new BarrelMeltingRecipe(
                meltingRecipe.input,
                meltingRecipe.output,
                meltingRecipe.heat.torch,
                meltingRecipe.heat.fire,
                meltingRecipe.heat.lava
        ));
    }
}