package subsistence.common.recipe.loader;

import com.google.gson.annotations.SerializedName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.wrapper.stack.GenericItem;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BarrelLoader {

    public static class Recipe {

        /* INPUTS */
        public GenericItem[] inputItem;
        public FluidStack inputFluid;

        /* OUTPUT */
        public ItemStack output;

        public String conditional = "all"; // all/any/any_with_global_limit

        @SerializedName("global_limit")
        public int globalLimit;
    }

    public static void parseFile(File file, String type) {
        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            Recipe[] recipes = JsonUtil.gson().fromJson(new FileReader(file), Recipe[].class);
            for (Recipe recipe : recipes) {

            }
        } catch (IOException ex) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
        }
    }

    private static void verifyParse(String name, Recipe recipe) {

    }
}