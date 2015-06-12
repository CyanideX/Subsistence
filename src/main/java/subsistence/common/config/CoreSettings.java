package subsistence.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.util.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CoreSettings {

    public static CoreSettings STATIC;

    public int barrelRain = 10;
    public int handCrank = 1;
    public int waterMill = 2;
    public int processRate = 20;
    public int wormwoodDry = 2400;
    public int compostBucketSize = 1;
    public boolean updateChecker = true;
    public boolean dumpItems = false;
    public int woodenBucketUses = 8;
    public int leafSaplingChance = 10; // Out of 100
    public int leafSaplingDoubleChance = 5; // Out of 100
    public boolean crashOnInvalidData = true;

    public static class Loader {

        public static void parse(File file) {
            if (!file.exists()) {
                STATIC = new CoreSettings();
            } else {
                try {
                    SubsistenceLogger.info("Parsing " + file.getName());
                    CoreSettings.STATIC = JsonUtil.gson().fromJson(new FileReader(file), CoreSettings.class);
                } catch (IOException e) {
                    SubsistenceLogger.warn("Failed to parse " + file.getName());
                    e.printStackTrace();
                }
            }
        }

        public static void makeNewFile(File file) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                SubsistenceLogger.info("Creating " + file.getName());
                CoreSettings obj = new CoreSettings();
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
}
