package subsistence.common.recipe.wrapper;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import subsistence.common.lib.RandomStack;
import subsistence.common.util.StackHelper;

import java.util.List;

public class SieveRecipe {

    private final ItemStack input;
    private final RandomStack[] output;
    private final int durationBlock;
    private final int durationHand;

    private String type;

    public SieveRecipe(ItemStack input, RandomStack[] output, int durationBlock, int durationHand, String type) {
        this.input = input;
        this.output = output;
        this.durationBlock = durationBlock;
        this.durationHand = durationHand;
        this.type = type;
    }

    public int getDurationBlock() {
        return durationBlock;
    }

    public int getDurationHand() {
        return durationHand;
    }

    public boolean valid(ItemStack stack, boolean table) {
        if (!table) {
            if (!("both".equals(type) || "hand".equals(type)))
                return false;
        } else {
            if (!("both".equals(type) || "block".equals(type)))
                return false;
        }
        return StackHelper.areStacksSimilar(stack, input, true);
    }

    public ItemStack[] get() {
        List<ItemStack> out = Lists.newArrayList();

        for (RandomStack stack : output) {
            out.add(stack.get());
        }

        return out.toArray(new ItemStack[out.size()]);
    }
}