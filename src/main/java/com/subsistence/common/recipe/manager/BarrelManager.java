package com.subsistence.common.recipe.manager;

import com.subsistence.common.recipe.wrapper.BarrelStoneRecipe;
import com.subsistence.common.recipe.wrapper.BarrelWoodRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dmillerw
 */
public class BarrelManager {

    private List<BarrelWoodRecipe> recipeWood = new ArrayList<BarrelWoodRecipe>();
    private List<BarrelStoneRecipe> recipeStone = new ArrayList<BarrelStoneRecipe>();

    public BarrelWoodRecipe getWooden(FluidStack fluid, ItemStack[] stack) {
        for (BarrelWoodRecipe recipe : recipeWood) {
            if (recipe.valid(fluid, stack)) {
                return recipe;
            }
        }
        return null;
    }

    public BarrelStoneRecipe getStone(FluidStack fluid, ItemStack[] stack) {
        for (BarrelStoneRecipe recipe : recipeStone) {
            if (recipe.valid(fluid, stack)) {
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

    public boolean isAllowed(ItemStack itemCopy) {
        for (BarrelStoneRecipe recipe : recipeStone) {
            for (ItemStack stack : recipe.getInputItem()) {
                if (itemCopy.getItem() == stack.getItem()) {
                    return true;
                }
            }
        }
        for (BarrelWoodRecipe recipe : recipeWood) {
            for (ItemStack stack : recipe.getInputItem()) {
                if (itemCopy.getItem() == stack.getItem()) {
                    return true;
                }
            }
        }
        return false;
    }
}
