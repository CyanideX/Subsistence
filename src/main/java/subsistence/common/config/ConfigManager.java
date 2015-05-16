package subsistence.common.config;

import subsistence.Subsistence;
import subsistence.common.recipe.core.*;

import java.io.File;

public class ConfigManager {
    public static File mainFile = new File(Subsistence.configPath, "main.json");
    public static File recipes = new File(Subsistence.configPath, "recipes/");
    public static void loadAllFiles () {
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

    private static void genDefaultConfigs () {
         if (!recipes.exists()) {
             recipes.mkdirs();
         }
        if (!mainFile.exists()) {
            MainSettings.makeNewFile(mainFile);
        }
        //TODO: make the rest of the stuff
    }

    private static void loadFile (String typeAndSubDir) {
        File[] recipeFiles = new File(recipes, typeAndSubDir).listFiles();
        if (recipeFiles != null) {
            for (File file : recipeFiles) {
                String fileType = file.getName().substring(file.getName().lastIndexOf(".")+1);
                if (fileType.equalsIgnoreCase("json")) {
                    String type = typeAndSubDir.substring(0,typeAndSubDir.lastIndexOf("/"));
                    if (type.equalsIgnoreCase("sieve")) {
                        SieveParser.parseFile(file);
                    } else if (type.equalsIgnoreCase("table")) {
                        TableParser.parseFile(file, typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/")));
                    } else if (type.equalsIgnoreCase("barrel")) {
                        BarrelParser.parseFile(file);
                    } else if (type.equalsIgnoreCase("compost")) {
                        CompostParser.parseFile(file);
                    } else if (type.equalsIgnoreCase("metalpress")) {
                        MetalPressParser.parseFile(file);
                    }
                }
            }
        }
    }
    public static void tryDumpItems (File file) {
        if (MainSettings.dumpItems) {

        }
    }
}
