package subsistence.common.recipe.loader;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.CompostRecipe;
import subsistence.common.recipe.wrapper.stack.GenericItem;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static subsistence.common.recipe.core.ErrorHandler.Loader.fail;
import static subsistence.common.recipe.core.ErrorHandler.Loader.info;

public class CompostLoader {

    public static class Recipe {

        public GenericItem input;
        public Output output;
        public Time time;

        public String type = "both"; // wood/stone/both
        public String conditional = "all"; // all/any/any_with_global_limit
        @SerializedName("global_limit")
        public int globalLimit = 0;

        public boolean condensates = false;
        @SerializedName("requires_condensate")
        public boolean requiresCondensate = false;
    }

    public static class Output {

        public ItemStack item;
        public FluidStack fluid;
    }

    public static class Time {

        public int compost = -1;
        public Heat heat = new Heat();

        public static class Heat {

            public int torch = -1;
            public int fire = -1;
            public int lava = -1;
            public boolean empty() {
                return torch == -1 && fire == -1 && lava == -1;
            }
        }
    }

    public static void parseFile(File file) {
        try {
            info("Compost", "Parsing " + file.getName());
            Recipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), Recipe[].class);

            for (Recipe recipe : recipes) {
                verifyParse(file.getName(), recipe);
            }
        } catch (IOException ex) {
            fail("Compost", "Failed to parse " + file.getName());
        }
    }

    private static void verifyParse(String name, Recipe recipe) {
        // Heat check - Heat can only be used in a stone barrel
        if (!recipe.time.heat.empty() && (recipe.type.equalsIgnoreCase("both") || recipe.type.equalsIgnoreCase("wood"))) {
            fail("Compost", "Heat can not be used in a wooden compost bin");
            return;
        }

        // Condensate check - Condensation can only occur with a heat source
        if (recipe.condensates && !recipe.time.heat.empty()) {
            fail("Compost", "Condensation recipe requires heat");
            return;
        }

        SubsistenceRecipes.COMPOST.register(
                new CompostRecipe(
                        GenericItem.merge(recipe.input).contents,
                        recipe.output.item,
                        recipe.output.fluid,
                        recipe.time.compost,
                        recipe.time.heat.torch,
                        recipe.time.heat.lava,
                        recipe.time.heat.fire,
                        recipe.type,
                        recipe.condensates,
                        recipe.requiresCondensate,
                        recipe.conditional,
                        recipe.globalLimit
                ));
    }
}
