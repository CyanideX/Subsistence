package oldStuff.common.lib;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oldStuff.SubsistenceOld;

public class SubsistenceLogger {
    
    private static final Logger log = LogManager.getLogger(SubsistenceOld.NAME);

    public static void log(Level level, Object object, Object... args) {
        log.log(level, String.valueOf(object), args);
    }

    public static void all(Object object, Object... args) {
        log(Level.ALL, object, args);
    }

    public static void debug(Object object, Object... args) {
        log(Level.DEBUG, object, args);
    }

    public static void error(Object object, Object... args) {
        log(Level.ERROR, object, args);
    }

    public static void fatal(Object object, Object... args) {
        log(Level.FATAL, object, args);
    }

    public static void info(Object object, Object... args) {
        log(Level.INFO, object, args);
    }

    public static void off(Object object, Object... args) {
        log(Level.OFF, object, args);
    }

    public static void trace(Object object, Object... args) {
        log(Level.TRACE, object, args);
    }

    public static void warn(Object object, Object... args) {
        log(Level.WARN, object, args);
    }
}