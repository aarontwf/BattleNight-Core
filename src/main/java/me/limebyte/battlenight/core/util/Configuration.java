package me.limebyte.battlenight.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    public static File playerFile;
    public static FileConfiguration players;

    public static void init() {
        try {
            playerFile = new File(BattleNight.instance.getDataFolder() + "/PluginData", "players.dat");

            if (!playerFile.exists()) {
                playerFile.getParentFile().mkdirs();
                copy(BattleNight.instance.getResource("players.dat"), playerFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            players.load(playerFile);
            BattleNight.reloadClasses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // YAML Save Method
    public static void saveYamls() {
        try {
            players.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveYAML(ConfigFile file) {
        try {
            if (file.equals(ConfigFile.Players))
                players.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ConfigFile {
        Players;
    }

}
