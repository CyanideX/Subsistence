package subsistence.common.config;

import com.google.common.collect.Maps;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import subsistence.common.item.SubsistenceItems;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * @author dmillerw
 */
public class ToolSettings {

    private static Map<Item, Data> hammers = Maps.newHashMap();
    private static Map<Item, Data> axes = Maps.newHashMap();

    private static void registerHammer(Item item, int strength, int damage) {
        hammers.put(item, new Data(strength, damage));
    }

    private static void registerAxe(Item item, int strength, int damage) {
        axes.put(item, new Data(strength, damage));
    }

    private static class Tools {

        public JsonData[] hammers;
        public JsonData[] axes;
    }

    private static class Data {

        public int strength;
        public int damage;

        public Data(int strength, int damage) {
            this.strength = strength;
            this.damage = damage;
        }
    }

    private static class JsonData {

        public Item item;

        public int strength;
        public int damage;
    }

    public static void initialize(File file) {
        // Hard coding! Whooooop
        registerHammer(SubsistenceItems.hammerWood, 1, 1);
        registerHammer(SubsistenceItems.hammerStone, 1, 1);
        registerHammer(SubsistenceItems.hammerSteel, 1, 1);
        registerHammer(SubsistenceItems.hammerIron, 1, 1);
        registerHammer(SubsistenceItems.hammerDiamond, 1, 1);

        registerAxe(Items.wooden_axe, 1, 1);
        registerAxe(Items.stone_axe, 1, 1);
        registerAxe(Items.iron_axe, 1, 1);
        registerAxe(Items.diamond_axe, 1, 1);
        registerAxe(Items.golden_axe, 1, 1);

        if (!file.exists())
            return;

        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            Tools tools = JsonUtil.gson().fromJson(new FileReader(file), Tools.class);

            for (JsonData hammer : tools.hammers) {
                registerHammer(hammer.item, hammer.strength, hammer.damage);
            }

            for (JsonData axe : tools.axes) {
                registerAxe(axe.item, axe.strength, axe.damage);
            }
        } catch (IOException e) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            e.printStackTrace();
        }
    }

    public static boolean isHammer(ItemStack itemStack) {
        return hammers.containsKey(itemStack.getItem());
    }

    public static boolean isAxe(ItemStack itemStack) {
        return axes.containsKey(itemStack.getItem());
    }

    public static int getHammerStrength(ItemStack itemStack) {
        return isHammer(itemStack) ? hammers.get(itemStack.getItem()).strength : 0;
    }

    public static int getHammerDamage(ItemStack itemStack) {
        return isHammer(itemStack) ? hammers.get(itemStack.getItem()).damage : 0;
    }

    public static int getAxeStrength(ItemStack itemStack) {
        return isAxe(itemStack) ? axes.get(itemStack.getItem()).strength : 0;
    }

    public static int getAxeDamage(ItemStack itemStack) {
        return isAxe(itemStack) ? axes.get(itemStack.getItem()).damage : 0;
    }
}
