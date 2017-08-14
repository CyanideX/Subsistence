package oldStuff.common.config;

import net.minecraftforge.common.config.Configuration;
import oldStuff.common.lib.SubsistenceLogger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CoreSettings {

    // Simply add new variables to this class to have them added to the config
    public static int barrelRain = 10;
    public static int handCrank = 1;
    public static int waterMill = 2;
    public static int processRate = 20;
    public static int wormwoodDry = 2400;
    public static int compostBucketSize = 1;
    public static boolean updateChecker = true;
    public static boolean dumpItems = false;
    public static int woodenBucketUses = 8;
    public static int saplingSearchDuration = 20;
    public static boolean crashOnInvalidData = true;

    public static void load(File file) {
        Configuration configuration = new Configuration(file);
        try {
            configuration.load();

            for (Field field : CoreSettings.class.getDeclaredFields()) {
                try {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (field.getType() == int.class) {
                            field.setInt(CoreSettings.class, configuration.get("general", field.getName(), (Integer) field.get(CoreSettings.class)).getInt());
                        } else if (field.getType() == boolean.class) {
                            field.setBoolean(CoreSettings.class, configuration.get("general", field.getName(), (Boolean) field.get(CoreSettings.class)).getBoolean());
                        }
                    }
                } catch (Exception ex) {
                    SubsistenceLogger.warn(String.format("Failed to load '%s' from config. Assuming default value", field.getName()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (configuration.hasChanged())
                configuration.save();
        }
    }
}
