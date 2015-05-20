package subsistence.common.config;

import cpw.mods.fml.common.registry.GameData;
import org.apache.commons.io.FileUtils;
import subsistence.Subsistence;
import subsistence.common.lib.ExtensionFilter;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.loader.*;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author MattDahEpic
 */
public class ConfigManager {

    public static File mainFile = new File(Subsistence.configPath, "main.json");
    public static File recipes = new File(Subsistence.configPath, "recipes/");
    public static File itemDump = new File(Subsistence.configPath, "key_dump.txt");

    public static void loadAllFiles() {
        genDefaultConfigs();

        CoreSettings.Loader.parse(mainFile);

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

            //TODO Copying from JAR fails. Investigate
            //maybe make it from url to File so that it can access the disk? @dmillerw
            try {
                final URL recipesDir = ConfigManager.class.getResource("/assets/subsistence/recipes/");
                final URLConnection urlConnection = recipesDir.openConnection();

                if (urlConnection instanceof JarURLConnection) {
                    final JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
                    final JarFile jarFile = jarURLConnection.getJarFile();

                    final String path = jarURLConnection.getEntryName();

                    while (jarFile.entries().hasMoreElements()) {
                        JarEntry entry = jarFile.entries().nextElement();

                        if (entry.getName().startsWith(path)) {
                            final String fileName = entry.getName().substring(path.length());

                            if (entry.isDirectory()) {
                                final File dir = new File(recipes, fileName);
                                if (!dir.exists())
                                    dir.mkdir();
                            } else {
                                final InputStream entryInput = jarFile.getInputStream(entry);
                                FileUtils.copyInputStreamToFile(entryInput, new File(recipes, fileName));
                            }
                        }
                    }
                } else if (urlConnection instanceof FileURLConnection) { //TODO: FileURLConection is technically internal
                                                                         //      may not exist on all platforms?
                    FileUtils.copyDirectory(new File(recipesDir.getPath()), recipes);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (!mainFile.exists()) {
            CoreSettings.Loader.makeNewFile(mainFile);
        }
    }

    public static void resetLoaded () {
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

                List<String> keys = new ArrayList<String>();

                for (Object obj : GameData.getItemRegistry().getKeys()) {
                    keys.add(obj.toString());
                }

                Collections.sort(keys);

                for (String str : keys) {
                    fileWriter.write(str + '\n');
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
