package subsistence.common.lib;

import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * @author dmillerw
 */
public class RandomStack {

    private static final Random random = new Random();

    public final ItemStack stack;

    private final float chance;

    public RandomStack(ItemStack stack, float chance) {
        if (chance < 0F)
            chance = 0F;
        if (chance > 1F)
            chance = 1F;

        this.stack = stack;
        this.chance = chance;
    }

    public ItemStack get() {
        return random.nextFloat() <= chance ? stack.copy() : null;
    }
}
