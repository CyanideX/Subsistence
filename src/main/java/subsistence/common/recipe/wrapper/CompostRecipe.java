package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.ItemHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @author lclc98
 */
public class CompostRecipe {

    private final ItemStack[] inputItem;

    private final ItemStack outputItem;
    private final FluidStack outputLiquid;

    private final int time;
    private final int timeTorch;
    private final int timeLava;
    private final int timeFire;

    public final String type;

    public CompostRecipe(ItemStack[] input, ItemStack outputItem, FluidStack outputLiquid, int time) {
        this(input, outputItem, outputLiquid, time, -1, -1, -1, "wood");
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire) {
        this(inputItem, outputItem, outputLiquid, time, timeTorch, timeLava, timeFire, "stone");
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire, String type) {
        List<ItemStack> merged = ItemHelper.mergeLikeItems(Arrays.asList(inputItem));
        this.inputItem = merged.toArray(new ItemStack[merged.size()]);

        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;

        this.time = time;
        this.timeTorch = timeTorch;
        this.timeLava = timeLava;
        this.timeFire = timeFire;

        this.type = type;
    }

    public boolean valid(ItemStack[] currentStack) {
        for (ItemStack stack : currentStack) {
            if (stack != null) {

                for (ItemStack check : inputItem) {
                    if (check.getItem() != stack.getItem() || check.getItemDamage() != stack.getItemDamage() || check.stackSize != stack.stackSize) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public int getTime() {
        return time;
    }

    public int getTimeTorch() {
        return timeTorch;
    }

    public int getTimeLava() {
        return timeLava;
    }

    public int getTimeFire() {
        return timeFire;
    }

    public boolean requiresHeat() {
        return timeTorch > 0 || timeLava > 0 || timeFire > 0;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FluidStack getOutputLiquid() {
        return outputLiquid;
    }
}
