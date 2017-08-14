package oldStuff.common.network;

import oldStuff.SubsistenceOld;
import oldStuff.common.lib.SubsistenceLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateChecker {

    public static boolean updateAvaliable = false;

    public static void checkForUpdate() {
        String remoteVersion = "";
        try {
            URL updateUrl = new URL("https://raw.githubusercontent.com/CyanideX/SubsistenceOld/master/version.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(updateUrl.openStream()));
            remoteVersion = reader.readLine();
            reader.close();
        } catch (Exception e) {
            SubsistenceLogger.error("Error during attempted update check!");
        }
        updateAvaliable = !remoteVersion.equalsIgnoreCase(SubsistenceOld.VERSION);
    }
}
