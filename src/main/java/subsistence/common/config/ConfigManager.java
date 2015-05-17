package subsistence.common.config;

import subsistence.Subsistence;
import subsistence.common.lib.ExtensionFilter;

import java.io.File;

public class ConfigManager {

    public static File mainFile = new File(Subsistence.configPath, "main.json");
    public static File recipes = new File(Subsistence.configPath, "recipes/");

    public static void loadAllFiles() {
        genDefaultConfigs();

        MainSettings.parseMainSettings(mainFile);

        loadFile("sieve/");
        loadFile("barrel/");
        loadFile("compost/");
        loadFile("metalpress/");

        loadFile("table/hammer");
        loadFile("table/drying");
        loadFile("table/axe");
    }

    private static void genDefaultConfigs() {
        if (!recipes.exists()) {
            recipes.mkdirs();
        }
        if (!mainFile.exists()) {
            MainSettings.makeNewFile(mainFile);
        }
        //TODO: make the rest of the stuff
        File dirBarrel = new File(recipes, "barrel/");
        if (!dirBarrel.exists()) {
            BarrelConfig.makeNewFiles();
        }
        File dirCompost = new File(recipes, "compost/");
        if (!dirCompost.exists()) {
            CompostConfig.makeNewFiles();
        }
        File dirMetalPress = new File(recipes, "metalpress/");
        if (!dirMetalPress.exists()) {
            MetalPressConfig.makeNewFiles();
        }
        File dirSieve = new File(recipes, "sieve/");
        if (!dirSieve.exists()) {
            SieveConfig.makeNewFiles();
        }
        File dirTable = new File(recipes, "table/");
        if (!dirTable.exists()) {
            TableConfig.makeNewFiles();
        }
    }

    private static void loadFile(String typeAndSubDir) {
        final File recipeDir = new File(recipes, typeAndSubDir);
        final String type = typeAndSubDir.substring(0, typeAndSubDir.lastIndexOf("/"));

        if (recipeDir.isDirectory()) {
            for (File file : recipeDir.listFiles(ExtensionFilter.JSON)) {
                if (type.equalsIgnoreCase("sieve")) {
                    SieveConfig.parseFile(file);
                } else if (type.equalsIgnoreCase("table")) {
                    TableConfig.parseFile(file, typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/")));
                } else if (type.equalsIgnoreCase("barrel")) {
                    BarrelConfig.parseFile(file);
                } else if (type.equalsIgnoreCase("compost")) {
                    CompostConfig.parseFile(file);
                } else if (type.equalsIgnoreCase("metalpress")) {
                    MetalPressConfig.parseFile(file);
                }
            }
        }
    }

    public void tryDumpItems(File file) {
        if (MainSettingsStatic.dumpItems) {

        }
    }
}
