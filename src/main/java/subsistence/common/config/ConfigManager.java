package subsistence.common.config;

import org.apache.commons.io.FileUtils;
import subsistence.Subsistence;
import subsistence.common.lib.ExtensionFilter;
import sun.net.www.protocol.file.FileURLConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author MattDahEpic
 */
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

            //TODO Copying from JAR fails. Investigate
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
            MainSettings.makeNewFile(mainFile);
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
                    TableConfig.parseFile(file, typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/") + 1));
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
