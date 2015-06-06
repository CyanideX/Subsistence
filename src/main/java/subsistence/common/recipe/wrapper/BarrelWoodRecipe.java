package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.ItemHelper;

public class BarrelWoodRecipe {

    public final ItemStack[] inputItem;
    public final FluidStack inputLiquid;
    public final ItemStack outputItem;
    public final FluidStack outputLiquid;

    public final String conditional;
    public final int globalLimit;

    public BarrelWoodRecipe(ItemStack[] inputItem, FluidStack inputLiquid, ItemStack outputItem, FluidStack outputLiquid, String conditional, int globalLimit) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.conditional = conditional;
        this.globalLimit = globalLimit;
    }

    public boolean valid(ItemStack[] currentStack, FluidStack fluidStack) {
        System.out.println("items valid == "+validItems(currentStack));
        System.out.println("fluid valid == "+validFluid(fluidStack));
        return validItems(currentStack) && validFluid(fluidStack);
    }

    private boolean validItems(ItemStack[] currentStack) {
        if ((inputItem != null && inputItem.length > 0) && (currentStack != null && currentStack.length > 0)) {
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
                    if (!found) {
                        return false;
                    }
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
        }
        return false;
    }

    private boolean validFluid(FluidStack fluidStack) {
        if (inputLiquid != null) {
            if (fluidStack == null) {
                return false;
            } else {
                return inputLiquid.isFluidEqual(fluidStack) && inputLiquid.amount <= fluidStack.amount;
            }
        } else {
            return true;
        }
    }
}
