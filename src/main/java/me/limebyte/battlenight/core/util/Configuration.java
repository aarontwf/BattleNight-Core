package me.limebyte.battlenight.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.config.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {

    public static File configFile;
    public static File classesFile;
    public static File waypointsFile;
    public static File playerFile;
    public static FileConfiguration config;
    public static FileConfiguration classes;
    public static FileConfiguration waypoints;
    public static FileConfiguration players;
    public static Config testConfig;

    public static void init() {
        try {
            configFile = new File(BattleNight.instance.getDataFolder(), "config.yml");
            classesFile = new File(BattleNight.instance.getDataFolder(), "classes.yml");
            waypointsFile = new File(BattleNight.instance.getDataFolder() + "/PluginData", "waypoints.dat");
            playerFile = new File(BattleNight.instance.getDataFolder() + "/PluginData", "players.dat");
            testConfig = new Config("test.yml");

            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                copy(BattleNight.instance.getResource("config.yml"), configFile);
            }
            if (!classesFile.exists()) {
                classesFile.getParentFile().mkdirs();
                copy(BattleNight.instance.getResource("classes.yml"), classesFile);
            }
            if (!waypointsFile.exists()) {
                waypointsFile.getParentFile().mkdirs();
                copy(BattleNight.instance.getResource("waypoints.dat"), waypointsFile);
            }
            if (!playerFile.exists()) {
                playerFile.getParentFile().mkdirs();
                copy(BattleNight.instance.getResource("players.dat"), playerFile);
            }

            testConfig.reloadConfig();
            testConfig.saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }

        config = new YamlConfiguration();
        classes = new YamlConfiguration();
        waypoints = new YamlConfiguration();
        players = new YamlConfiguration();
        reloadYamls();
    }

    public static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reloadYamls() {
        try {
            config.load(configFile);
            classes.load(classesFile);
            waypoints.load(waypointsFile);
            players.load(playerFile);
            BattleNight.reloadClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Waypoints Load Method
    public static void loadWaypoints() {
        try {
            waypoints.load(waypointsFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // YAML Save Method
    public static void saveYamls() {
        try {
            config.save(configFile);
            classes.save(classesFile);
            waypoints.save(waypointsFile);
            players.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveYAML(ConfigFile file) {
        try {
            if (file.equals(ConfigFile.Main))
                config.save(configFile);
            if (file.equals(ConfigFile.Classes))
                classes.save(classesFile);
            if (file.equals(ConfigFile.Waypoints))
                waypoints.save(waypointsFile);
            if (file.equals(ConfigFile.Players))
                players.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ConfigFile {
        Main, Classes, Waypoints, Players
    }

}
