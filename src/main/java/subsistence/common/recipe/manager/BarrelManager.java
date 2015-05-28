package subsistence.common.recipe.manager;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.recipe.wrapper.BarrelMeltingRecipe;
import subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import subsistence.common.recipe.wrapper.BarrelWoodRecipe;

import java.util.List;

public class BarrelManager {

    private List<BarrelWoodRecipe> recipeWood = Lists.newArrayList();
    private List<BarrelStoneRecipe> recipeStone = Lists.newArrayList();
    private List<BarrelMeltingRecipe> recipeMelting = Lists.newArrayList();

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


    public BarrelMeltingRecipe getMelting(ItemStack itemStack) {
        for (BarrelMeltingRecipe recipe : recipeMelting) {
            if (recipe.valid(itemStack)) {
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

    public void registerMelting(BarrelMeltingRecipe barrelRecipe) {
        recipeMelting.add(barrelRecipe);
    }

    public void clear() {
        recipeStone.clear();
        recipeWood.clear();
        recipeMelting.clear();
    }
}
