package subsistence.common.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.registry.GameData;
import org.apache.commons.io.FileUtils;
import subsistence.Subsistence;
import subsistence.common.lib.ExtensionFilter;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.loader.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static File mainFile = new File(Subsistence.configPath, "main.json");
    public static File heatFile = new File(Subsistence.configPath, "heat.json");
    public static File recipes = new File(Subsistence.configPath, "recipes/");
    public static File itemDump = new File(Subsistence.configPath, "key_dump.txt");

    public static void loadAllFiles() {
        genDefaultConfigs();

        CoreSettings.Loader.parse(mainFile);
        HeatSettings.initialize(heatFile);

        loadFile("sieve/");
        loadFile("barrel/");
        loadFile("compost/");
        loadFile("metalpress/");

        loadFile("table/hammer");
        loadFile("table/drying");
        loadFile("table/axe");

        tryDumpItems(itemDump);
    }

    private static void genDefaultConfigs() {
        if (!recipes.exists()) {
            recipes.mkdirs();

            final String path = "assets/subsistence/recipes";
            final URL url = ConfigManager.class.getResource("/" + path);
            if (url != null) {
                try {
                    FileUtils.copyDirectory(new File(url.toURI()), recipes);
                } catch (URISyntaxException ignore) {
                } catch (IOException ignore) {
                }
            }
        }

        if (!mainFile.exists()) {
            CoreSettings.Loader.makeNewFile(mainFile);
        }
    }

    public static void resetLoaded() {
        SubsistenceRecipes.SIEVE.clear();
        SubsistenceRecipes.TABLE.clear();
        SubsistenceRecipes.BARREL.clear();
        SubsistenceRecipes.COMPOST.clear();
        SubsistenceRecipes.METAL_PRESS.clear();
        SubsistenceRecipes.PERISHABLE.clear();
    }

    private static void loadFile(String typeAndSubDir) {
        final File recipeDir = new File(recipes, typeAndSubDir);
        final String type = typeAndSubDir.substring(0, typeAndSubDir.lastIndexOf("/"));

        if (recipeDir.isDirectory()) {
            for (File file : recipeDir.listFiles(ExtensionFilter.JSON)) {
                if (type.equalsIgnoreCase("sieve")) {
                    SieveLoader.parseFile(file);
                } else if (type.equalsIgnoreCase("table")) {
                    TableLoader.parseFile(file, typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/") + 1));
                } else if (type.equalsIgnoreCase("barrel")) {
                    BarrelLoader.parseFile(file);
                } else if (type.equalsIgnoreCase("compost")) {
                    CompostLoader.parseFile(file);
                } else if (type.equalsIgnoreCase("metalpress")) {
                    MetalPressLoader.parseFile(file);
                }
            }
        }
    }

    public static void tryDumpItems(File file) {
        try {
            if (CoreSettings.STATIC.dumpItems) {
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    file.delete();
                }

                FileWriter fileWriter = new FileWriter(file);

                Map<String, List<String>> items = Maps.newHashMap();

                for (Object obj : GameData.getItemRegistry().getKeys()) {
                    String item = obj.toString();
                    String[] split;

                    if (item.contains(":")) {
                        split = item.split(":");
                    } else {
                        split = new String[]{"Misc", item};
                    }

                    List<String> list = items.get(split[0]);
                    if (list == null) list = Lists.newArrayList();
                    list.add(split[1]);
                    items.put(split[0], list);
                }

                List<String> keys = new ArrayList<String>(items.keySet());
                Collections.sort(keys);

                boolean first = true;

                for (String key : keys) {
                    if (!first)
                        fileWriter.append("\n");
                    fileWriter.append("[").append(key).append("]\n");
                    first = false;

                    List<String> list = items.get(key);
                    Collections.sort(list);

                    for (String item : list) {
                        fileWriter.append(item).append("\n");
                    }
                }

                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException ex) {
            SubsistenceLogger.error("Failed to dump items.");
            SubsistenceLogger.trace(ex.fillInStackTrace());
        }
    }
}
