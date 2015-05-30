package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import subsistence.common.util.StackHelper;

public class TableAxeRecipe {

    public final ItemStack input;
    public final ItemStack output;

    public final int durability;

    public TableAxeRecipe(ItemStack input, ItemStack output, int durability) {
        this.input = input;
        this.output = output;
        this.durability = durability;
    }

    public boolean isInputStack(ItemStack stack) {
        return stack != null && StackHelper.areStacksSimilar(stack, input, true);
    }
}