package oldStuff.common.recipe.manager;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import oldStuff.common.recipe.wrapper.TableAxeRecipe;
import oldStuff.common.recipe.wrapper.TableDryingRecipe;
import oldStuff.common.recipe.wrapper.TableSmashingRecipe;

import java.util.List;

public class TableManager {

    private List<TableSmashingRecipe> recipesSmashing = Lists.newArrayList();
    private List<TableDryingRecipe> recipesDrying = Lists.newArrayList();
    private List<TableAxeRecipe> recipesAxe = Lists.newArrayList();

    public void register(Object object) {
        if (object instanceof TableSmashingRecipe)
            recipesSmashing.add((TableSmashingRecipe) object);
        else if (object instanceof TableDryingRecipe)
            recipesDrying.add((TableDryingRecipe) object);
        else if (object instanceof TableAxeRecipe)
            recipesAxe.add((TableAxeRecipe) object);
    }

    public TableSmashingRecipe getSmashingRecipe(ItemStack input) {
        for (TableSmashingRecipe recipe : recipesSmashing) {
            if (recipe.isInputStack(input)) {
                return recipe;
            }
        }
        return null;
    }

    public TableDryingRecipe getDryingRecipe(ItemStack input) {
        for (TableDryingRecipe recipe : recipesDrying) {
            if (recipe.isInputStack(input)) {
                return recipe;
            }
        }
        return null;
    }

    public TableAxeRecipe getAxeRecipe(ItemStack input) {
        for (TableAxeRecipe recipe : recipesAxe) {
            if (recipe.isInputStack(input)) {
                return recipe;
            }
        }
        return null;
    }

    public void clear() {
        recipesSmashing.clear();
        recipesDrying.clear();
        recipesAxe.clear();
    }
}
