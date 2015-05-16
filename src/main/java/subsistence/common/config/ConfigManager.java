package subsistence.common.config;

import subsistence.Subsistence;
import subsistence.common.recipe.core.RecipeParser;

import java.io.File;

public class ConfigManager {
    public static File mainFile = new File(Subsistence.configPath, "main.json");
    public static File recipes = new File(Subsistence.configPath, "recipes/");
    public static void loadAllFiles () {
        genDefaultConfigs();

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
            //TODO: make main file with default values
        }
        //TODO: make the rest of the stuff
    }

    private static void loadFile (String typeAndSubDir) {
        File[] recipeFiles = new File(recipes, typeAndSubDir).listFiles();
        if (recipeFiles != null) {
            for (File file : recipeFiles) {
                String fileType = file.getName().substring(file.getName().lastIndexOf(".")+1);
                if (fileType.equalsIgnoreCase("json")) {
                    RecipeParser.parseFile(file,typeAndSubDir.substring(0,typeAndSubDir.lastIndexOf("/")),typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/"))); //TODO: make the type and subdir be one thing
                }
            }
        }
    }
}
