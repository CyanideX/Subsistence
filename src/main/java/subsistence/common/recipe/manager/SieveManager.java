package subsistence.common.recipe.manager;

import net.minecraft.item.ItemStack;
import subsistence.common.recipe.wrapper.SieveRecipe;

import java.util.ArrayList;
import java.util.List;

public class SieveManager {

    private List<SieveRecipe> recipes = new ArrayList<SieveRecipe>();

    public SieveRecipe get(ItemStack stack, boolean table) {
        for (SieveRecipe recipe : recipes) {
            if (recipe.valid(stack, table)) {
                return recipe;
            }
        }
        return null;
    }


    public void register(SieveRecipe barrelRecipe) {
        recipes.add(barrelRecipe);
    }


    public void clear() {
        recipes.clear();
    }
}
