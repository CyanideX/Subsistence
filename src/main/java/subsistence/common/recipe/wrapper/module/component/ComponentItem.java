package subsistence.common.recipe.wrapper.module.component;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import subsistence.common.recipe.wrapper.module.core.ModularObject;

/**
 * @author dmillerw
 */
public class ComponentItem extends ModularObject {

    public ItemStack itemStack;

    @Override
    public void acceptData(JsonObject jsonObject) {
        String item = getString(jsonObject, "item", "");
        int damage = getInt(jsonObject, "damage", 0);
        int amount = getInt(jsonObject, "amount", 1);

        itemStack = new ItemStack(itemFromString(item), amount, damage);
    }
}
