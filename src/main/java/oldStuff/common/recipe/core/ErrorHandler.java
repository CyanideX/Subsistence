package oldStuff.common.recipe.core;

import oldStuff.common.lib.SubsistenceLogger;

/**
 * @author dmillerw
 */
public class ErrorHandler {

    public static class Loader {

        public  static void info(String type, String msg) {
            SubsistenceLogger.info("[Recipe: " + type + "] " + msg);
        }

        public static void fail(String type, String msg) {
            SubsistenceLogger.warn("[Recipe: " + type + "] " + msg);
        }
    }
}
