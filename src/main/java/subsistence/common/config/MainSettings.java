package subsistence.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.util.JsonUtil;

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
    public int compostBucketSize = 1;
    public boolean dumpItems = false;

    public MainSettings() {
    }

    public static void parseMainSettings(File file) {
        try {
            SubsistenceLogger.info("Parsing " + file.getName());
            MainSettings settings = JsonUtil.gson().fromJson(new FileReader(file), MainSettings.class);
            MainSettingsStatic.barrelRain = settings.barrelRain;
            MainSettingsStatic.handCrank = settings.handCrank;
            MainSettingsStatic.waterMill = settings.waterMill;
            MainSettingsStatic.processRate = settings.processRate;
            MainSettingsStatic.wormwoodDry = settings.wormwoodDry;
            MainSettingsStatic.dumpItems = settings.dumpItems;
            MainSettingsStatic.compostBucketSize = settings.compostBucketSize;
        } catch (IOException e) {
            SubsistenceLogger.warn("Failed to parse " + file.getName());
            e.printStackTrace();
        }
    }

    public static void makeNewFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            SubsistenceLogger.info("Creating " + file.getName());
            MainSettings obj = new MainSettings();
            String json = gson.toJson(obj);
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            SubsistenceLogger.warn("Failed to write " + file.getName());
            e.printStackTrace();
        }
    }
}
