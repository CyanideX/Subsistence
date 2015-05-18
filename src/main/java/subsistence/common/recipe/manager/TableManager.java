package subsistence.common.recipe.manager;

import subsistence.common.lib.DurabilityMapping;
import subsistence.common.lib.tool.ToolDefinition;
import subsistence.common.recipe.wrapper.TableDryingRecipe;
import subsistence.common.recipe.wrapper.TableRecipe;
import subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class TableManager {

    private List<TableRecipe> recipesTable = new ArrayList<TableRecipe>();
    private List<TableDryingRecipe> recipesDrying = new ArrayList<TableDryingRecipe>();

    public void registerHammerRecipe(Object input, Object output, float durability, int speed, boolean table, boolean hammerMill) {
        registerRecipe(input, output, ToolDefinition.HAMMER, durability, speed, table, hammerMill);
    }

    public void registerRecipe(Object input, Object output, ToolDefinition tool, float durability, int speed, boolean table, boolean hammerMill) {
        if (input == null || output == null || tool == null || durability < 0F) {
            return;
        }

        ItemStack[] in = StackHelper.convert(input);
        ItemStack[] out = StackHelper.convert(output);

        if (out.length > 0) {
            for (ItemStack stack : in) {
                DurabilityMapping.INSTANCE.registerDurablity(stack, durability);
                register(new TableRecipe(stack, out[0], tool, speed, table, hammerMill, true, true));
            }
        }
    }

    public void register(TableRecipe recipe) {
        recipesTable.add(recipe);
    }

    public TableRecipe get(ItemStack input, ItemStack tool, boolean isTable) {
        for (TableRecipe recipe : recipesTable) {
            if (recipe.isInput(input, tool, isTable)) {
                return recipe;
            }
        }
        return null;
    }

    public TableRecipe get(ItemStack input, ToolDefinition tool, boolean isTable) {
        for (TableRecipe recipe : recipesTable) {
            if (recipe.isInput(input, tool, isTable)) {
                return recipe;
            }
        }
        return null;
    }


    public void registerDryingRecipe(Object input, Object output, int duration) {
        if (input == null || output == null || duration < 0F) {
            return;
        }

        ItemStack[] in = StackHelper.convert(input);
        ItemStack[] out = StackHelper.convert(output);

        if (out.length > 0) {
            for (ItemStack stack : in) {
                register(new TableDryingRecipe(stack, out[0], duration));
            }
        }
    }

    public void register(TableDryingRecipe recipe) {
        recipesDrying.add(recipe);
    }

    public TableDryingRecipe getDrying(ItemStack input) {
        for (TableDryingRecipe recipe : recipesDrying) {
            if (recipe.isInputStack(input)) {
                return recipe;
            }
        }
        return null;
    }

    public void clear() {
        recipesTable.clear();
        recipesDrying.clear();
    }

}
