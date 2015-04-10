package com.subsistence.common.recipe.core;

import com.subsistence.common.util.StackHelper;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dmillerw
 *         Temporary class?
 */
public class RecipeParser {


    public static void parseFile(File file, String type, String subType) {
        if (type.equalsIgnoreCase("sieve")) {
            SieveParser.parseFile(file);
        } else if (type.equalsIgnoreCase("table")) {
            TableParser.parseFile(file, subType);
        } else if (type.equalsIgnoreCase("barrel")) {
            BarrelParser.parseFile(file);
        }
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
            meta = Integer.parseInt(input_str.substring(input_str.indexOf(";") + 1));
            input_str = input_str.substring(0, input_str.indexOf(";"));
        }
        if (input_str.contains("/")) {
            quantity = Integer.parseInt(input_str.substring(input_str.indexOf("/") + 1));
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
}
