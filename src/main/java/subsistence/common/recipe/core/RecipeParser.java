package subsistence.common.recipe.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.util.StackHelper;

/**
 * @author dmillerw
 */
public class RecipeParser {

    public static Object getItem(String item) {

        ItemStack stack;
        String mod_id = "minecraft";
        String input_str = item;
        int meta = 0;
        int quantity = 1;

        if (input_str.contains(":")) {
            // has mod id
            mod_id = input_str.substring(0, input_str.indexOf(":"));
            input_str = input_str.substring(input_str.indexOf(":") + 1);
        }

        if (input_str.contains(";")) {
            meta = getInt(input_str, ';', 0);
            input_str = input_str.substring(0, input_str.indexOf(";"));
        }
        if (input_str.contains("/")) {
            quantity = getInt(input_str, '/', 1);
            input_str = input_str.substring(0, input_str.indexOf("/"));
        }

        if (item.startsWith("@")) {
            ItemStack[] convertStack = StackHelper.convert(input_str.replace("@", ""));
            for (ItemStack oreStack : convertStack) {
                oreStack.stackSize = quantity;
            }
            return convertStack;
        }

        stack = GameRegistry.findItemStack(mod_id, input_str, quantity);

        if (stack != null) {
            stack.setItemDamage(meta);
        }
        return stack;
    }

    public static Integer getInt(String text, Character symbol, int defaultInt) {
        int s = text.indexOf(symbol);
        if (s == -1)
            return defaultInt;
        for (int i = s + 1; i < text.length(); i++) {
            if (new String(new char[]{'/', ';'}).contains(Character.toString(text.charAt(i)))) {
                return Integer.parseInt(text.substring(s + 1, i));
            }
        }
        return Integer.parseInt(text.substring(s + 1));
    }

    public static FluidStack getLiquid(String liquid) {
        String input_str = liquid;
        int quantity = 1000;

        if (input_str.contains("/")) {
            quantity = getInt(input_str, '/', 1000);
            input_str = input_str.substring(0, input_str.indexOf("/"));
        }

        return new FluidStack(FluidRegistry.getFluid(input_str), quantity);
    }
}
