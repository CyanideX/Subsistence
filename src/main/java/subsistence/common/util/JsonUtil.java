package subsistence.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import subsistence.common.recipe.wrapper.module.core.ModularObject;

/**
 * @author dmillerw
 */
public class JsonUtil {

    private static Gson gson;

    public static Gson gson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();

            gsonBuilder.registerTypeAdapter(ModularObject.class, new ModularObject.Deserializer());

            gson = gsonBuilder.setPrettyPrinting().create();
        }
        return gson;
    }
}
