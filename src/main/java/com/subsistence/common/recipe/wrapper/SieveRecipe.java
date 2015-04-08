package com.subsistence.common.recipe.wrapper;

import com.subsistence.common.lib.RandomStack;
import com.subsistence.common.util.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SieveRecipe implements IGenericRecipe<ItemStack[]> {

    private final ItemStack input;
    private final RandomStack[] outputBlock;
    private final RandomStack[] outputHand;
    private final int durationBlock;
    private final int durationHand;

    private final boolean ignoreNBT;

    public SieveRecipe(ItemStack input, RandomStack[] outputBlock, RandomStack[] outputHand, int durationBlock, int durationHand, boolean ignoreNBT) {
        this.input = input;
        this.outputBlock = outputBlock;
        this.outputHand = outputHand;
        this.durationBlock = durationBlock;
        this.durationHand = durationHand;
        this.ignoreNBT = ignoreNBT;
    }

    public int getDurationBlock() {
        return durationBlock;
    }

    public int getDurationHand() {
        return durationHand;
    }

    @Override
    public boolean valid(ItemStack stack) {
        return StackHelper.areStacksSimilar(stack, input, ignoreNBT);
    }

    @Override
    public ItemStack[] get(ItemStack input, boolean block) {
        List<ItemStack> out = new ArrayList<ItemStack>();

        if (block)
            for (RandomStack stack : outputBlock) {
                out.add(stack.get());
            }
        else
            for (RandomStack stack : outputHand) {
                out.add(stack.get());
            }

        return out.toArray(new ItemStack[out.size()]);
    }
}