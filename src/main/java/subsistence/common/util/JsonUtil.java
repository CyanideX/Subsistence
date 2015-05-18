package subsistence.common.util;

import com.google.gson.*;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Type;

/**
 * @author dmillerw
 */
public class JsonUtil {

    private static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();

            gsonBuilder.registerTypeAdapter(ItemStack.class, new ItemStackDeserializer());
            gsonBuilder.registerTypeAdapter(FluidStack.class, new FluidStackDeserializer());

            gson = gsonBuilder.setPrettyPrinting().create();
        }
        return gson;
    }

    public static class ItemStackDeserializer implements JsonDeserializer<ItemStack> {

        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                throw new JsonParseException("Cannot deserialize ItemStack from " + json.getClass().getSimpleName());
            }

            JsonObject object = json.getAsJsonObject();

            if (!object.has("item")) {
                throw new JsonParseException("ItemStack json object is missing 'item' key");
            }

            String item = object.get("item").getAsString();
            int damage = object.has("damage") ? object.get("damage").getAsInt() : 0;
            int amount = object.has("amount") ? object.get("amount").getAsInt() : 0;

            if (!item.contains(":")) {
                item = "minecraft:" + item;
            }

            return new ItemStack(GameData.getItemRegistry().getObject(item), amount, damage);
        }
    }

    public static class FluidStackDeserializer implements JsonDeserializer<FluidStack> {

        @Override
        public FluidStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                throw new JsonParseException("Cannot deserialize FluidStack from " + json.getClass().getSimpleName());
            }

            JsonObject object = json.getAsJsonObject();

            if (!object.has("fluid")) {
                throw new JsonParseException("ItemStack json object is missing 'fluid' key");
            }

            String fluid = object.get("item").getAsString();
            int amount = object.has("amount") ? object.get("amount").getAsInt() : 0;

            return new FluidStack(FluidRegistry.getFluid(fluid), amount);
        }
    }
}
