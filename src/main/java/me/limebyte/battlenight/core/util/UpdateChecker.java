package me.limebyte.battlenight.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.plugin.PluginDescriptionFile;

public class UpdateChecker {

    private static final String VERSION_URL = "https://raw.github.com/BattleNight/BattleNight-Core/master/version.txt";
    private static final String UPDATE_MESSAGE = "A new version of BattleNight has been released.  You can download v%s from %s.";

    private static String version;
    private static boolean snapshot;

    private static boolean updateAvailable = false;
    private static String updateMessage;

    public static void check(BattleNightAPI api, PluginDescriptionFile pdf) {
        UpdateChecker.version = pdf.getVersion();
        UpdateChecker.snapshot = removeSuffix();

        Messenger messenger = api.getMessenger();

        try {
            URL update = new URL(VERSION_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(update.openStream()));
            String latestVersion = in.readLine().trim();
            String downloadURL = in.readLine().trim();
            in.close();

            if (isNewer(latestVersion)) {
                updateAvailable = true;
                updateMessage = String.format(UPDATE_MESSAGE, latestVersion, downloadURL);
                messenger.log(Level.INFO, updateMessage);
            }
        } catch (Exception e) {
            messenger.debug(Level.WARNING, "Failed to check for updates.");
            return;
        }
    }

    private static boolean isNewer(String latestVersion) {
        if (version.equals(latestVersion)) return snapshot ? true : false;

        String[] verInts = version.split(".");
        String[] testInts = latestVersion.split(".");

        if (verInts.length != 3 || testInts.length != 3) return false;

        for (int i = 0; i < 3; i++) {
            int ver = Integer.parseInt(verInts[i]);
            int test = Integer.parseInt(testInts[i]);

            if (test > ver) return true;
        }

        return false;
    }

    private static boolean removeSuffix() {
        boolean snapshot = version.contains("-");
        version = version.split("-")[0];
        return snapshot;
    }

    public static boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public static String getUpdateMessage() {
        return updateMessage;
    }
}
