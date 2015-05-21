package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.ItemHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @author lclc98
 */
public class CompostRecipe {

    public final ItemStack[] inputItem;

    private final ItemStack outputItem;
    private final FluidStack outputLiquid;

    private final int time;
    private final int timeTorch;
    private final int timeLava;
    private final int timeFire;

    public final String type;
    public final boolean condensates;
    public final boolean requiresCondensate;

    public CompostRecipe(ItemStack[] input, ItemStack outputItem, FluidStack outputLiquid, int time) {
        this(input, outputItem, outputLiquid, time, -1, -1, -1, "wood", false, false);
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire, boolean condensates, boolean requiresCondensate) {
        this(inputItem, outputItem, outputLiquid, time, timeTorch, timeLava, timeFire, "stone", condensates, requiresCondensate);
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire, String type, boolean condensates, boolean requiresCondensate) {
        List<ItemStack> merged = ItemHelper.mergeLikeItems(Arrays.asList(inputItem));
        this.inputItem = merged.toArray(new ItemStack[merged.size()]);

        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;

        this.time = time;
        this.timeTorch = timeTorch;
        this.timeLava = timeLava;
        this.timeFire = timeFire;

        this.type = type;
        this.condensates = condensates;
        this.requiresCondensate = requiresCondensate;
    }

    public boolean valid(ItemStack[] currentStack, FluidStack fluidStack) {
        if (requiresCondensate && (fluidStack == null || fluidStack.getFluid() == null || fluidStack.getFluid() != FluidRegistry.WATER || fluidStack.amount != FluidContainerRegistry.BUCKET_VOLUME))
            return false;

        for (ItemStack stack : currentStack) {
            if (stack != null) {
                boolean found = false;

                for (ItemStack check : inputItem) {
                    if (check.getItem() == stack.getItem() && check.getItemDamage() == stack.getItemDamage() && check.stackSize == stack.stackSize) {
                        found = true;
                    }
                }

                if (!found)
                    return false;
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
