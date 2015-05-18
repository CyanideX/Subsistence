package subsistence.common.recipe.wrapper.module.process;

import com.google.gson.JsonObject;
import subsistence.common.recipe.wrapper.module.core.ModularObject;

/**
 * @author dmillerw
 */
public class ProcessMelt extends ModularObject {

    public int time;
    public int heat;

    @Override
    public void acceptData(JsonObject jsonObject) {
        time = getInt(jsonObject, "time", 1);
        heat = getInt(jsonObject, "heat", 1);
    }
}
