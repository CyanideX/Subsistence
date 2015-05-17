package subsistence.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cpw.mods.fml.common.FMLLog;
import subsistence.common.config.staticvals.MainSettingsStatic;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author MattDahEpic
 */
public class MainSettings {
    public int barrelRain = 10;
    public int handCrank = 1;
    public int waterMill = 2;
    public int processRate = 20;
    public int wormwoodDry = 2400;
    public boolean dumpItems = false;

    public MainSettings () {}

    public static void parseMainSettings (File file) {
        try {
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            MainSettings settings = new Gson().fromJson(new FileReader(file), MainSettings.class);
            MainSettingsStatic.barrelRain = settings.barrelRain;
            MainSettingsStatic.handCrank = settings.handCrank;
            MainSettingsStatic.waterMill = settings.waterMill;
            MainSettingsStatic.processRate = settings.processRate;
            MainSettingsStatic.wormwoodDry = settings.wormwoodDry;
            MainSettingsStatic.dumpItems = settings.dumpItems;
        } catch (IOException e) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
            e.printStackTrace();
        }
    }
    public static void makeNewFile (File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FMLLog.info("[Subsistence] Creating "+file.getName());
            MainSettings obj = new MainSettings();
            String json = gson.toJson(obj);
            System.out.println("Created JSON is: "+json);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            FMLLog.warning("[Subsistence] Failed to write "+file.getName());
            e.printStackTrace();
        }
    }
}
