package subsistence.common.recipe.manager;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import subsistence.common.recipe.wrapper.SieveRecipe;

import java.util.List;

public class SieveManager {

    private List<SieveRecipe> recipes = Lists.newArrayList();

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
