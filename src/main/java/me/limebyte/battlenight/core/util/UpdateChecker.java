package me.limebyte.battlenight.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateChecker {

    private static final int PROJECT_ID = 31742;

    private static final String API_HOST = "https://api.curseforge.com/servermods/files?projectIds=";
    private static final String API_NAME_VALUE = "name";
    private static final String API_LINK_VALUE = "downloadUrl";
    private static final String API_GAME_VERSION_VALUE = "gameVersion";

    private static final String UPDATE_MESSAGE = "A new version of BattleNight has been released.  You can download v%s for %s from %s.";

    private static String version;
    private static boolean snapshot;

    private static boolean updateAvailable = false;
    private static String updateMessage;

    public static void check(BattleNightAPI api, PluginDescriptionFile pdf) {
        UpdateChecker.version = pdf.getVersion();
        UpdateChecker.snapshot = removeSuffix();

        Messenger messenger = api.getMessenger();

        try {
            URL url = new URL(API_HOST + PROJECT_ID);
            URLConnection conn = url.openConnection();

            conn.addRequestProperty("User-Agent", "BattleNight Update Checker");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = reader.readLine();

            JSONArray array = (JSONArray) JSONValue.parse(response);
            JSONObject latest = (JSONObject) array.get(array.size() - 1);

            String version = ((String) latest.get(API_NAME_VALUE)).replace("BattleNight v", "");
            String downloadUrl = (String) latest.get(API_LINK_VALUE);
            String gameVersion = (String) latest.get(API_GAME_VERSION_VALUE);

            if (isNewer(version)) {
                updateAvailable = true;
                updateMessage = String.format(UPDATE_MESSAGE, version, gameVersion, downloadUrl);
                messenger.log(Level.INFO, updateMessage);
            }
        } catch (Exception e) {
            messenger.debug(Level.WARNING, "Failed to check for updates.");
            messenger.debug(Level.WARNING, e.getMessage());
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
