package subsistence.common.recipe.wrapper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.wrapper.stack.GenericItem;

public class BarrelMeltingRecipe {

    public final GenericItem input;
    public FluidStack output;

    public int timeTorch;
    public int timeFire;
    public int timeLava;

    public BarrelMeltingRecipe(GenericItem input, FluidStack output, int timeTorch, int timeFire, int timeLava) {
        this.input = input;
        this.output = output;
        this.timeTorch = timeTorch;
        this.timeFire = timeFire;
        this.timeLava = timeLava;
    }

    public boolean valid(ItemStack stack) {
        if (stack == null) {
            return false;
        }

        for (ItemStack input : this.input.contents) {
            if (input != null && stack.isItemEqual(input)) {
                return true;
            }
        }
        return false;
    }
}
