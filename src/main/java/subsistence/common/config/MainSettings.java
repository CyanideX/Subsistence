package subsistence.common.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import cpw.mods.fml.common.FMLLog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author MattDahEpic
 */
public class MainSettings {
    public static int barrelRain = 10;
    public static int handCrank = 1;
    public static int waterMill = 2;
    public static int processRate = 20;
    public static int wormwoodDry = 2400;
    public static boolean dumpItems = false;

    public MainSettings () {}

    public static void parseMainSettings (File file) {
        try {
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            MainSettings settings = new Gson().fromJson(new FileReader(file), MainSettings.class);
            barrelRain = settings.barrelRain;
            handCrank = settings.handCrank;
            waterMill = settings.waterMill;
            processRate = settings.processRate;
            wormwoodDry = settings.wormwoodDry;
            dumpItems = settings.dumpItems;
        } catch (IOException e) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
        }
        System.out.println("barren rain == "+barrelRain);
    }
    public static void makeNewFile (File file) {
        Gson gson = new Gson();
        Type type = new TypeToken<MainSettings>() {}.getType();
        try {
            FMLLog.info("[Subsistence] Creating "+file.getName());
            gson.toJson(MainSettings.class,type,new JsonWriter(new FileWriter(file)));
        } catch (IOException e) {
            FMLLog.warning("[Subsistence] Failed to write "+file.getName());
        }
    }
}
