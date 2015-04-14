package com.subsistence.common.recipe.core;

import com.google.gson.Gson;
import com.subsistence.common.tile.machine.TileBarrel;
import cpw.mods.fml.common.FMLLog;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author lclc98
 */
public class GeneralParser {

    public static class ParsedGeneral {

        public int barrelRain;
    }

    public static void parseFile(File file) {
        try {
            FMLLog.info("[Subsistence] Parsing " + file.getName());
            ParsedGeneral recipe = new Gson().fromJson(new FileReader(file), ParsedGeneral.class);
            verifyParse(file.getName(), recipe);
        } catch (IOException ex) {
            FMLLog.warning("[Subsistence] Failed to parse " + file.getName());
        }
    }

    private static void verifyParse(String name, ParsedGeneral recipe) {
        TileBarrel.rain = recipe.barrelRain;
    }
}
