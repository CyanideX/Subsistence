package subsistence.common.recipe.wrapper;

import subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;

/**
 * @author lclc98
 */
public class TableDryingRecipe {


    private final ItemStack input;
    private final ItemStack output;
    private final int duration;

    public TableDryingRecipe(ItemStack input, ItemStack output, int duration) {
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    public boolean isInputStack(ItemStack stack) {
        return stack != null && StackHelper.areStacksSimilar(stack, input, true);
    }

    public float getDuration() {
        return duration;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInputItem() {
        return input;
    }
}