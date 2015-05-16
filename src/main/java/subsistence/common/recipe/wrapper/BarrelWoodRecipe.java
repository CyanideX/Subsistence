package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author lclc98
 */
public class BarrelWoodRecipe {
    private final ItemStack[] inputItem;
    private final FluidStack inputLiquid;
    private final ItemStack outputItem;
    private final FluidStack outputLiquid;
    private final int time;

    public BarrelWoodRecipe(ItemStack[] inputItem, FluidStack inputLiquid, ItemStack outputItem, FluidStack outputLiquid, int time) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.time = time;
    }

    public boolean valid(FluidStack fluid, ItemStack[] currentStack) {
        if (currentStack == null)
            return false;
        ArrayList<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(this.inputItem));
        for (ItemStack stack : currentStack) {
            if (stack != null) {
                boolean ret = false;

                for (ItemStack s : list) {
                    if (s != null && s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage()) {
                        ret = true;
                        list.remove(s);
                        break;
                    }
                }

                if (!ret) {
                    return false;
                }
            }
        }

        if (this.inputLiquid != null)
            return list.isEmpty() && fluid != null && fluid.containsFluid(this.inputLiquid);
        else
            return list.isEmpty();
    }

    public int getTime() {
        return time;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FluidStack getOutputLiquid() {
        return outputLiquid;
    }

    public ItemStack[] getInputItem() {
        return inputItem;
    }
}
