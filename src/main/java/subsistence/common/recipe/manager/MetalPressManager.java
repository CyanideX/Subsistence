package subsistence.common.recipe.manager;

import net.minecraft.item.ItemStack;
import subsistence.common.recipe.wrapper.MetalPressRecipe;

import java.util.ArrayList;
import java.util.List;

public class MetalPressManager {

    private List<MetalPressRecipe> recipes = new ArrayList<MetalPressRecipe>();

    public MetalPressRecipe get(ItemStack stack) {
        for (MetalPressRecipe recipe : recipes) {
            if (recipe.valid(stack)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(MetalPressRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }

    public void clear() {
        recipes.clear();
    }

    public boolean isAllowed(ItemStack item) {
        for (MetalPressRecipe recipe : recipes) {
            if (item.getItem() == recipe.inputItem.getItem()) {
                return true;
            }
        }
        return false;
    }
}
