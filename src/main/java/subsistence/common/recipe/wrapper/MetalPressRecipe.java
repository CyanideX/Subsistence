package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;

public class MetalPressRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final int amount;

    public MetalPressRecipe(ItemStack inputItem, ItemStack outputItem, int amount) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.amount = amount;
    }

    public boolean valid(ItemStack input) {
        return input.getItem() == this.inputItem.getItem();
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public int getAmount() {
        return amount;
    }

    public ItemStack getInputItem() {
        return inputItem;
    }
}
