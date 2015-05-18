package subsistence.common.recipe.wrapper.module.core;

import com.google.common.collect.Maps;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.wrapper.module.component.ComponentFluid;
import subsistence.common.recipe.wrapper.module.component.ComponentItem;
import subsistence.common.recipe.wrapper.module.process.ProcessMelt;
import subsistence.common.recipe.wrapper.module.process.ProcessMix;

import java.util.Map;

/**
 * @author dmillerw
 */
public class ModularRegistry {

    private static final Map<String, Class<? extends ModularObject>> moduleMap = Maps.newHashMap();

    public static void register(String type, Class<? extends ModularObject> obj) {
        moduleMap.put(type, obj);
    }

    public static void initialize() {
        if (moduleMap.isEmpty()) {
            // Generic
            register("generic.fluid", ComponentFluid.class);
            register("generic.item", ComponentItem.class);

            // Barrel
            register("barrel.process.melt", ProcessMelt.class);
            register("barrel.process.mix", ProcessMix.class);
        }
    }

    public static ModularObject create(String type) {
        try {
            ModularObject object = moduleMap.get(type).newInstance();
            object.type = type;
            return object;
        } catch (NullPointerException ex) {
            SubsistenceLogger.warn("Failed to find module of type: " + type);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
