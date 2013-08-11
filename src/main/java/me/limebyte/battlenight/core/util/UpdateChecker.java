package me.limebyte.battlenight.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.plugin.PluginDescriptionFile;

public class UpdateChecker {

    private static final String UPDATE_URL = "https://raw.github.com/BattleNight/BattleNight-Core/master/version.txt";
    private BattleNightAPI api;
    private String version;
    private boolean snapshot;

    public UpdateChecker(BattleNightAPI api, PluginDescriptionFile pdf) {
        this.api = api;
        version = pdf.getVersion();
        snapshot = removeSuffix();
    }

    public void check() {
        Messenger messenger = api.getMessenger();

        try {
            URL update = new URL(UPDATE_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(update.openStream()));
            String latestVersion = in.readLine().trim();
            in.close();

            if (isNewer(latestVersion)) {
                messenger.log(Level.INFO, "Update v" + latestVersion + " available!");
            }
        } catch (Exception e) {
            messenger.debug(Level.WARNING, "Failed to check for updates.");
            return;
        }
    }

    private boolean isNewer(String latestVersion) {
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

    private boolean removeSuffix() {
        boolean snapshot = version.contains("-");
        version = version.split("-")[0];
        return snapshot;
    }
}
