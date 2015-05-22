package subsistence.common.recipe.manager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.wrapper.CompostRecipe;

import java.util.ArrayList;
import java.util.List;

public class CompostManager {

    private List<CompostRecipe> recipes = new ArrayList<CompostRecipe>();

    private boolean canAccept(String tileType, String recipeType) {
        return recipeType.equals("both") || tileType.equals(recipeType);
    }

    public CompostRecipe get(String type, ItemStack[] stack, FluidStack fluidStack) {
        for (CompostRecipe recipe : recipes) {
            if (canAccept(type, recipe.type) && recipe.valid(stack, fluidStack)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(CompostRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }

    public void clear() {
        recipes.clear();
    }
}
