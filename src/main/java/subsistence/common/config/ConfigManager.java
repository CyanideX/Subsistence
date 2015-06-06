package subsistence.common.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import subsistence.Subsistence;
import subsistence.common.lib.ExtensionFilter;
import subsistence.common.lib.SubsistenceLogger;
import subsistence.common.recipe.SubsistenceRecipes;
import subsistence.common.recipe.loader.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConfigManager {

    public static final File configDir = new File(Subsistence.configPath);
    public static final File mainFile = new File(Subsistence.configPath, "main.json");
    public static final File heatFile = new File(Subsistence.configPath, "heat.json");
    public static final File toolsFile = new File(Subsistence.configPath, "tools.json");
    public static final File recipes = new File(Subsistence.configPath, "recipes/");
    public static final File itemDump = new File(Subsistence.configPath, "key_dump.txt");

    public static void preInit() {
        genDefaultConfigs();
        CoreSettings.Loader.parse(mainFile);
    }

    public static void postInit() {
        HeatSettings.initialize(heatFile);
        ToolSettings.initialize(toolsFile);

        // TEMPORARY TO ALLOW FOR HOSTING ON CURSEFORGE
        if (World.class.getName().contains("World") || Loader.isModLoaded("IC2")) {
            loadFile("sieve/");

            loadFile("hammer_mill/");

            loadFile("barrel/wood");
            loadFile("barrel/stone");
            loadFile("barrel/melting");

            loadFile("compost/");

            loadFile("metalpress/");

            loadFile("table/smash");
            loadFile("table/dry");
            loadFile("table/axe");
        } else {
            SubsistenceLogger.info("Failed to detect IC2. Until further notice, recipe handling has been disabled without it!");
            SubsistenceLogger.info("Go download IC2!");
            SubsistenceLogger.info("Do it, do it nooooow!");
        }

        tryDumpItems(itemDump);
    }

    private static void genDefaultConfigs() {
        if (!recipes.exists()) {
            recipes.mkdirs();

            File to = new File(recipes, "defaultrecipes.zip");
            copyFromJar(Subsistence.class, "subsistence/defaultrecipes.zip", to);

            extractZip(to);
            safeDelete(to);
        }

        if (!mainFile.exists()) {
            CoreSettings.Loader.makeNewFile(mainFile);
        }
    }

    /**
     * Taken from EnderCore
     * 
     * @author tterrag
     * 
     * @param jarClass
     *            - A class from the jar in question
     * @param filename
     *            - Name of the file to copy, automatically prepended with "/assets/"
     * @param to
     *            - File to copy to
     */
    public static void copyFromJar(Class<?> jarClass, String filename, File to) {
        SubsistenceLogger.info("Copying file " + filename + " from jar");
        URL url = jarClass.getResource("/assets/" + filename);

        try {
            FileUtils.copyURLToFile(url, to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Ilias Tsagklis
     *         <p>
     *         From <a href= "http://examples.javacodegeeks.com/core-java/util/zip/extract-zip-file-with-subdirectories/" > this site.</a>
     * 
     * @param zip
     *            - The zip file to extract
     * 
     * @return The folder extracted to
     */
    public static File extractZip(File zip) {
        String zipPath = zip.getParent();
        File temp = new File(zipPath);
        temp.mkdir();

        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(zip);

            // get an enumeration of the ZIP file entries
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();

                File destinationPath = new File(zipPath, entry.getName());

                // create parent directories
                destinationPath.getParentFile().mkdirs();

                // if the entry is a file extract it
                if (entry.isDirectory()) {
                    continue;
                } else {
                    SubsistenceLogger.info("Extracting file: " + destinationPath);

                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];

                    FileOutputStream fos = new FileOutputStream(destinationPath);

                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }

                    bos.close();
                    bis.close();
                }
            }
        } catch (IOException e) {
            SubsistenceLogger.error("Error opening zip file" + e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
                SubsistenceLogger.error("Error while closing zip file" + e);
            }
        }

        return temp;
    }

    public static void safeDelete(File file) {
        try {
            file.delete();
        } catch (Exception e) {
            SubsistenceLogger.error("Deleting file " + file.getAbsolutePath() + " failed.");
        }
    }

    public static void safeDeleteDirectory(File file) {
        try {
            FileUtils.deleteDirectory(file);
        } catch (Exception e) {
            SubsistenceLogger.error("Deleting directory " + file.getAbsolutePath() + " failed.");
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
                    BarrelLoader.parseFile(file, typeAndSubDir.substring(typeAndSubDir.lastIndexOf("/") + 1));
                } else if (type.equalsIgnoreCase("compost")) {
                    CompostLoader.parseFile(file);
                } else if (type.equalsIgnoreCase("metalpress")) {
                    MetalPressLoader.parseFile(file);
                } else if (type.equalsIgnoreCase("hammer_mill")) {
                    HammerMillLoader.parseFile(file);
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
                    if (obj != null) {
                        String item = obj.toString();
                        String[] split;

                        if (item.contains(":")) {
                            split = item.split(":");
                        } else {
                            split = new String[] { "Misc", item };
                        }

                        List<String> list = items.get(split[0]);
                        if (list == null)
                            list = Lists.newArrayList();
                        list.add(split[1]);
                        items.put(split[0], list);
                    }
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
