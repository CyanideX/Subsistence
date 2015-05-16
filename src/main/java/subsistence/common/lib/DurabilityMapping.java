package subsistence.common.lib;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dmillerw
 */
public class DurabilityMapping {

    public static final DurabilityMapping INSTANCE = new DurabilityMapping();

    private Map<ItemStack, Float> durabilityMapping = new HashMap<ItemStack, Float>();

    public void registerDurablity(ItemStack stack, float durability) {
        if (getDurability(stack) == 1F) return;
        durabilityMapping.put(stack, durability);
    }

    public float getDurability(ItemStack stack) {
        for (Map.Entry<ItemStack, Float> entry : durabilityMapping.entrySet()) {
            if (entry.getKey().isItemEqual(stack)) return entry.getValue();
        }
        return 1F;
    }
}
