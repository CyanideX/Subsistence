package oldStuff.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import oldStuff.common.util.StackHelper;

public class TableDryingRecipe {

    public final ItemStack input;
    public final ItemStack output;

    public final int duration;

    public TableDryingRecipe(ItemStack input, ItemStack output, int duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    public boolean isInputStack(ItemStack stack) {
        return stack != null && StackHelper.areStacksSimilar(stack, input, true);
    }
}