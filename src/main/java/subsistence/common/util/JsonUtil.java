package subsistence.common.util;

import com.google.common.collect.Lists;
import com.google.gson.*;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import subsistence.common.config.CoreSettings;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.core.ErrorHandler;
import subsistence.common.recipe.wrapper.stack.GenericItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();

            gsonBuilder.registerTypeAdapter(GenericItem.class, new GenericItemDeserializer());
            gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackDeserializer());
            gsonBuilder.registerTypeAdapter(FluidStack.class, new FluidStackDeserializer());
            gsonBuilder.registerTypeAdapter(Item.class, new ItemDeserializer());

            gson = gsonBuilder.setPrettyPrinting().create();
        }
        return gson;
    }

    public static class GenericItemDeserializer implements JsonDeserializer<GenericItem> {

        @Override
        public GenericItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ItemStack[] contents = new ItemStack[0];
            if (json.isJsonPrimitive()) {
                boolean resolved = false;
                String string = json.getAsString();

                if (string.contains(":")) { // Item? or ore tag
                    if (string.startsWith("ore:")) {
                        string = string.substring(4);
                        // will fall through to the ore tag detection
                    } else {
                        contents = new ItemStack[1];
                        contents[0] = new ItemStack(GameData.getItemRegistry().getObject(string));
                        resolved = true;
                    }
                } else { // If not, try appending 'minecraft:' and see if it's a valid item
                    Item item = GameData.getItemRegistry().getObject("minecraft:" + string);
                    if (item != null) {
                        contents = new ItemStack[1];
                        contents[0] = new ItemStack(item);
                        resolved = true;
                    }
                }

                if (!resolved) {
                    // If we've gotten here, it's just an ore dictionary tag
                    ArrayList<ItemStack> ores = OreDictionary.getOres(string);
                    contents = ores.toArray(new ItemStack[ores.size()]);
                }
            } else if (json.isJsonArray()) {
                GenericItem[] array = context.deserialize(json, GenericItem[].class);
                return GenericItem.merge(array);
            } else if (json.isJsonObject()) {// See if it's a full ItemStack or just an ore tag
                JsonObject object = json.getAsJsonObject();
                if (object.has("item")) {
                    contents = new ItemStack[1];
                    contents[0] = context.deserialize(json, ItemStack.class);
                } else if (object.has("ores")) {
                    JsonArray array = object.getAsJsonArray("ores");
                    int size = object.has("amount") ? object.get("amount").getAsInt() : 1;
                    List<ItemStack> ores = Lists.newArrayList();

                    for (int i = 0; i < array.size(); i++) {
                        String ore = array.get(i).getAsString();
                        for (ItemStack stack : OreDictionary.getOres(ore)) {
                            ItemStack copy = stack.copy();
                            copy.stackSize = size;
                            ores.add(stack);
                        }
                    }

                    contents = ores.toArray(new ItemStack[ores.size()]);
                }
            }

            GenericItem genericStack = new GenericItem();
            genericStack.contents = contents;
            return genericStack;
        }
    }

    public static class ItemStackDeserializer implements JsonDeserializer<ItemStack> {

        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                if (json.isJsonPrimitive()) {
                    String string = json.getAsString();

                    if (!string.contains(":")) {
                        string = "minecraft:" + string;
                    }

                    Item item = GameData.getItemRegistry().getObject(string);
                    if (item == null) {
                        return fail(string + " is not a valid item!");
                    }

                    return new ItemStack(item);
                } else {
                    return fail("Cannot deserialize ItemStack from " + json.getClass().getSimpleName());
                }
            } else {
                JsonObject object = json.getAsJsonObject();

                if (!object.has("item")) {
                    return fail("ItemStack json object is missing 'item' key");
                }

                String itemString = object.get("item").getAsString();
                int damage = object.has("damage") ? object.get("damage").getAsInt() : 0;
                int amount = object.has("amount") ? object.get("amount").getAsInt() : 1;

                if (!itemString.contains(":")) {
                    itemString = "minecraft:" + itemString;
                }

                Item item = GameData.getItemRegistry().getObject(itemString);
                if (item == null) {
                    return fail(itemString + " is not a valid item!");
                }

                return ItemHelper.sanitizeStack(new ItemStack(GameData.getItemRegistry().getObject(itemString), amount, damage));
            }
        }
    }

    public static class FluidStackDeserializer implements JsonDeserializer<FluidStack> {

        @Override
        public FluidStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                if (json.isJsonPrimitive()) {
                    String string = json.getAsString();

                    Fluid fluid = FluidRegistry.getFluid(string);
                    if (fluid == null) {
                        return fail(string + " is not a valid fluid!");
                    }

                    return new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
                } else {
                    return fail("Cannot deserialize FluidStack from " + json.getClass().getSimpleName());
                }
            } else {
                JsonObject object = json.getAsJsonObject();

                if (!object.has("fluid")) {
                    fail("ItemStack json object is missing 'fluid' key");
                }

                String fluidString = object.get("fluid").getAsString();
                int amount = object.has("amount") ? object.get("amount").getAsInt() : FluidContainerRegistry.BUCKET_VOLUME;

                Fluid fluid = FluidRegistry.getFluid(fluidString);
                if (fluid == null) {
                    return fail(fluidString + " is not a valid fluid!");
                }

                return new FluidStack(FluidRegistry.getFluid(fluidString), amount);
            }
        }
    }

    private static <T> T fail(String msg, Object... fmt) {
        String err = String.format(msg, fmt);
        if (CoreSettings.STATIC.crashOnInvalidData) {
            throw new JsonParseException(err);
        } else {
            SubsistenceLogger.error(err);
            SubsistenceLogger.info("Swallowing error due to config...");
        }
        return null;
    }

    public static class ItemDeserializer implements JsonDeserializer<Item> {

        @Override
        public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                String tag = json.getAsString();
                if (!tag.contains(":")) {
                    tag = "minecraft:" + tag;
                }
                return GameData.getItemRegistry().getObject(tag);
            } else {
                return null;
            }
        }
    }
}
