package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;

public class MetalPressRecipe {

    public final ItemStack inputItem;
    public final ItemStack outputItem;
    public final int amount;

    public MetalPressRecipe(ItemStack inputItem, ItemStack outputItem, int amount) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.amount = amount;
    }

    public boolean valid(ItemStack input) {
        return input.getItem() == this.inputItem.getItem();
    }
}
