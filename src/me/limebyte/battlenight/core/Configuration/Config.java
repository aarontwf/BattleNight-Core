package me.limebyte.battlenight.core.Configuration;

import me.limebyte.battlenight.core.BattleNight;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
public class Config {

    // FileConfigurations
    FileConfiguration main = null;
    FileConfiguration classes = null;
    FileConfiguration tracks = null;
    FileConfiguration waypoints = null;
    FileConfiguration players = null;

    // Files
    File mainFile = null;
    File classesFile = null;
    File tracksFile = null;
    File waypointsFile = null;
    File playersFile = null;

    // Get Main Class
    public static BattleNight plugin;
    public Config(BattleNight instance) {
        plugin = instance;
    }

    public enum ConfigFile {
        MAIN(plugin.getDataFolder().getPath(), "Config.yml"),
        CLASSES(plugin.getDataFolder().getPath(), "Classes.yml"),
        TRACKS(plugin.getDataFolder().getPath(), "Tracks.yml"),
        WAYPOINTS(plugin.getDataFolder() + "/PluginData", "Waypoints.dat"),
        PLAYERS(plugin.getDataFolder() + "/PluginData", "Players.dat");

        private ConfigFile(String path, String file) {
            this.p = path;
            this.f = file;
        }

        public final String p;
        public final String f;

        public String getFilePath() {
            return p;
        }

        public String getFileName() {
            return f;
        }
    }

    private FileConfiguration getFileConfiguration(ConfigFile cf) {
        switch (cf) {
            case MAIN:
                return main;
            case CLASSES:
                return classes;
            case TRACKS:
                return tracks;
            case WAYPOINTS:
                return waypoints;
            case PLAYERS:
                return players;
            default:
                return null;
        }
    }

    private File getFile(ConfigFile cf) {
        switch (cf) {
            case MAIN:
                return mainFile;
            case CLASSES:
                return classesFile;
            case TRACKS:
                return tracksFile;
            case WAYPOINTS:
                return waypointsFile;
            case PLAYERS:
                return playersFile;
            default:
                return null;
        }
    }

    public void reload(ConfigFile cf) {
        FileConfiguration fileConfig = getFileConfiguration(cf);
        File file = getFile(cf);
        if (file == null) {
            file = new File(cf.getFilePath(), cf.getFileName());
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(cf.getFileName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfig.setDefaults(defConfig);
        }
    }

    public FileConfiguration get(ConfigFile cf) {
        if (getFileConfiguration(cf) == null) reload(cf);
        return (getFileConfiguration(cf));
    }

    public void save(ConfigFile cf) {
        FileConfiguration fileConfig = getFileConfiguration(cf);
        File file = getFile(cf);
        if (fileConfig == null || file == null) reload(cf);
        try {
            fileConfig.save(file);
        } catch (IOException ex) {
            plugin.log.severe(String.format("Could not save %s config to %s.", cf.toString(), cf.getFileName()));
        }
    }
}
