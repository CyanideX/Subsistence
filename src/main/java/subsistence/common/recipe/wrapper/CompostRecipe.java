package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.ItemHelper;

import java.util.Arrays;
import java.util.List;

public class CompostRecipe {

    //TODO Make a builder for this mess

    public final ItemStack[] inputItem;

    public final ItemStack outputItem;
    public final FluidStack outputLiquid;

    public final int time;
    public final int timeTorch;
    public final int timeLava;
    public final int timeFire;

    public final String type;
    public final String conditional;
    public final boolean condensates;
    public final boolean requiresCondensate;

    public final int globalLimit;

    public CompostRecipe(ItemStack[] input, ItemStack outputItem, FluidStack outputLiquid, int time, String conditional, int globalLimit) {
        this(input, outputItem, outputLiquid, time, -1, -1, -1, "wood", false, false, conditional, globalLimit);
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire, boolean condensates, boolean requiresCondensate, String conditional, int globalLimit) {
        this(inputItem, outputItem, outputLiquid, time, timeTorch, timeLava, timeFire, "stone", condensates, requiresCondensate, conditional, globalLimit);
    }

    public CompostRecipe(ItemStack[] inputItem, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire, String type, boolean condensates, boolean requiresCondensate, String conditional, int globalLimit) {
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
        this.conditional = conditional;
        this.globalLimit = globalLimit;
    }

    public boolean valid(ItemStack[] currentStack, FluidStack fluidStack) {
        if (requiresCondensate && (fluidStack == null || fluidStack.getFluid() == null || fluidStack.getFluid() != FluidRegistry.WATER || fluidStack.amount != FluidContainerRegistry.BUCKET_VOLUME))
            return false;

        if (conditional.equals("all")) {
            for (ItemStack required : inputItem) {
                boolean found = false;

                for (ItemStack content : currentStack) {
                    if (content == null)
                        continue;

                    if (ItemHelper.areItemsEqual(content, required) && required.stackSize == content.stackSize) {
                        found = true;
                    }
                }

                if (!found)
                    return false;
            }

            return true;
        } else if (conditional.equals("any")) {
            for (ItemStack required : inputItem) {
                for (ItemStack content : currentStack) {
                    if (content == null)
                        continue;

                    if (ItemHelper.areItemsEqual(content, required) && required.stackSize == content.stackSize) {
                        return true;
                    }
                }
            }

            return false;
        } else if (conditional.equals("any_with_global_limit")) {
            int found = 0;
            for (ItemStack content : currentStack) {
                for (ItemStack required : inputItem) {
                    if (ItemHelper.areItemsEqual(content, required)) {
                        found += content.stackSize;
                        if (found == globalLimit) {
                            return true;
                        } else if (found > globalLimit) {
                            return false;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean requiresHeat() {
        return timeTorch > 0 || timeLava > 0 || timeFire > 0;
    }

    public boolean validInput(ItemStack[] input) {
        int matches = 0;
        for (ItemStack inputItem : input) {
            for (ItemStack recipeItem : this.inputItem) {
                if (inputItem == recipeItem) {
                    matches++;
                }
            }
        }
        return matches > 0;
    }
}
