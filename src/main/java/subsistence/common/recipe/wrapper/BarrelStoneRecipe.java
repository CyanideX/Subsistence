package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.ItemHelper;

public class BarrelStoneRecipe {

    public final ItemStack[] inputItem;
    public final FluidStack inputLiquid;
    public final ItemStack outputItem;
    public final FluidStack outputLiquid;

    public final String conditional;
    public final int globalLimit;

    public final int timeTorch;
    public final int timeFire;
    public final int timeLava;

    public BarrelStoneRecipe(ItemStack[] inputItem, FluidStack inputLiquid, ItemStack outputItem, FluidStack outputLiquid, String conditional, int globalLimit, int timeTorch, int timeFire, int timeLava) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.conditional = conditional;
        this.globalLimit = globalLimit;
        this.timeTorch = timeTorch;
        this.timeFire = timeFire;
        this.timeLava = timeLava;
    }

    public boolean valid(ItemStack[] currentStack, FluidStack fluidStack) {
        if (!fluidStack.isFluidEqual(inputLiquid))
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
}
