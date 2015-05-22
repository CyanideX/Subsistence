package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class BarrelStoneRecipe {

    private final ItemStack[] inputItem;
    private final FluidStack inputLiquid;
    private final ItemStack outputItem;
    private final FluidStack outputLiquid;
    private final int time;
    private final int timeTorch;
    private final int timeLava;
    private final int timeFire;

    public BarrelStoneRecipe(ItemStack[] inputItem, FluidStack inputLiquid, ItemStack outputItem, FluidStack outputLiquid, int time, int timeTorch, int timeLava, int timeFire) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;

        this.time = time;
        this.timeTorch = timeTorch;
        this.timeLava = timeLava;
        this.timeFire = timeFire;
    }

    public boolean valid(FluidStack fluid, ItemStack[] currentStack) {
        if (currentStack == null)
            return false;
        List<ItemStack> list = Arrays.asList(this.inputItem);
        for (ItemStack stack : currentStack) {
            if (stack != null) {
                boolean ret = false;

                for (ItemStack s : list) {
                    if (s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage()) {
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

    public int getTimeTorch() {
        return timeTorch;
    }

    public int getTimeLava() {
        return timeLava;
    }

    public int getTimeFire() {
        return timeFire;
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
