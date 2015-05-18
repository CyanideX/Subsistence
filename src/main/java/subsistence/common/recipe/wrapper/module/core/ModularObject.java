package subsistence.common.recipe.wrapper.module.core;

import com.google.gson.*;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import subsistence.common.lib.SubsistenceLogger;

import java.lang.reflect.Type;

/**
 * @author dmillerw
 */
public abstract class ModularObject {

    private static ModularObject BLANK;

    static {
        BLANK = new ModularObject() {
            @Override
            public void acceptData(JsonObject jsonObject) {

            }
        };
        BLANK.type = "blank";
    }

    public String type;

    public abstract void acceptData(JsonObject jsonObject);

    public final String getString(JsonObject jsonObject, String key, String def) {
        if (!jsonObject.has(key)) {
            return def;
        } else {
            return jsonObject.get(key).getAsString();
        }
    }

    public final int getInt(JsonObject jsonObject, String key, int def) {
        if (!jsonObject.has(key)) {
            return def;
        } else {
            return jsonObject.get(key).getAsInt();
        }
    }

    public final Item itemFromString(String str) {
        if (!str.contains(":")) {
            str = "minecraft:" + str;
        }
        return GameData.getItemRegistry().getObject(str);
    }

    public static class Deserializer implements JsonDeserializer<ModularObject> {

        @Override
        public ModularObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (!json.isJsonObject()) {
                SubsistenceLogger.warn("Tried to parse ModularObject from something other than a JsonObject");
                return BLANK;
            }

            JsonObject object = json.getAsJsonObject();

            String type = object.get("object").getAsString();
            JsonObject data = object.getAsJsonObject("data");

            if (type == null || type.isEmpty()) {
                SubsistenceLogger.warn("ModularObject must contain a defined type!");
                return BLANK;
            }

            if (data == null) {
                data = new JsonObject();
            }

            ModularObject modularObject = ModularRegistry.create(type);

            if (modularObject == null) {
                SubsistenceLogger.warn("Failed to create a ModularObject of type: " + type);
                return BLANK;
            }

            modularObject.acceptData(data);

            return modularObject;
        }
    }
}
