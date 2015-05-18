package subsistence.common.recipe.wrapper.module.misc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import subsistence.common.recipe.core.RecipeParser;
import subsistence.common.recipe.wrapper.module.core.ModularObject;

/**
 * @author dmillerw
 */
public class Perishable extends ModularObject {

    public ModularObject item;
    public int duration;

    @Override
    public void acceptData(JsonObject jsonObject, JsonDeserializationContext context) {
        item = context.deserialize(jsonObject.getAsJsonObject("item"), ModularObject.class);
        if (!RecipeParser.checkTypes(item)) {
            item = null;
            // Log?
        }
        duration = getInt(jsonObject, "duration", 1);
    }
}
