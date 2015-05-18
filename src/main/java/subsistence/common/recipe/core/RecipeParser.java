package subsistence.common.recipe.core;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.wrapper.module.RestrictedType;
import subsistence.common.recipe.wrapper.module.core.ModularObject;
import subsistence.common.util.StackHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dmillerw
 */
public class RecipeParser {

    public static void resetParsed() {
        SubsistenceRecipes.SIEVE.clear();
        SubsistenceRecipes.TABLE.clear();
        SubsistenceRecipes.BARREL.clear();
        SubsistenceRecipes.COMPOST.clear();
        SubsistenceRecipes.METAL_PRESS.clear();
        SubsistenceRecipes.PERISHABLE.clear();
    }

    public static boolean checkTypes(Object instance) {
        //TODO Logging, proper dying, eetc

        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                if (field.getType().isArray()) {
                    if (field.getType().getComponentType().isAssignableFrom(ModularObject.class)) {
                        RestrictedType annotation = field.getAnnotation(RestrictedType.class);
                        if (annotation != null) {
                            ModularObject[] array = (ModularObject[]) field.get(instance);
                            for (int i=0; i<array.length; i++) {
                                ModularObject modularObject = array[i];
                                if (!Arrays.asList(annotation.value()).contains(modularObject.type)) {
                                    SubsistenceLogger.warn("Index '" + i + "' of array field '" + field.getName() + "' of recipe '" + clazz.getSimpleName() + "' cannot be of type '" + modularObject.type + "'");
                                    return false;
                                }
                            }
                        }
                    }
                } else {
                    if (field.getType().isAssignableFrom(ModularObject.class)) {
                        RestrictedType annotation = field.getAnnotation(RestrictedType.class);
                        if (annotation != null) {
                            ModularObject modularObject = (ModularObject) field.get(instance);
                            if (!Arrays.asList(annotation.value()).contains(modularObject.type)) {
                                SubsistenceLogger.warn("Field '" + field.getName() + "' of recipe '" + clazz.getSimpleName() + "' cannot be of type '" + modularObject.type + "'");
                                return false;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static void dumpItems(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        } else {
            file.delete();
        }

        FileWriter fileWriter = new FileWriter(file);

        List<String> keys = new ArrayList<String>();

        for (Object obj : GameData.getItemRegistry().getKeys()) {
            keys.add(obj.toString());
        }

        Collections.sort(keys);

        for (String str : keys) {
            fileWriter.write(str + '\n');
        }

        fileWriter.flush();
        fileWriter.close();
    }

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
