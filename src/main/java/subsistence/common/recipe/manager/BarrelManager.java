package subsistence.common.recipe.manager;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;

import java.util.ArrayList;
import java.util.List;

public class BarrelManager {

    private List<BarrelWoodRecipe> recipeWood = new ArrayList<BarrelWoodRecipe>();
    private List<BarrelStoneRecipe> recipeStone = new ArrayList<BarrelStoneRecipe>();

    public BarrelWoodRecipe getWooden(ItemStack[] stack, FluidStack fluid) {
        for (BarrelWoodRecipe recipe : recipeWood) {
            if (recipe.valid(stack, fluid)) {
                return recipe;
            }
        }
        return null;
    }

    public BarrelStoneRecipe getStone(ItemStack[] stack, FluidStack fluid) {
        for (BarrelStoneRecipe recipe : recipeStone) {
            if (recipe.valid(stack, fluid)) {
                return recipe;
            }
        }
        return null;
    }


    public void registerWood(BarrelWoodRecipe barrelRecipe) {
        recipeWood.add(barrelRecipe);
    }

    public void registerStone(BarrelStoneRecipe barrelRecipe) {
        recipeStone.add(barrelRecipe);
    }

    public void clear() {
        recipeStone.clear();
        recipeWood.clear();
    }
}
