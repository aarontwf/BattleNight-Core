package me.limebyte.battlenight.core.util.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    private final String fileName;

    private File file;
    private YamlConfiguration fileConfig;

    public Config(String fileName) {
        this.fileName = fileName;
    }

    public void reload() {
        if (file == null) {
            File dataFolder = BattleNight.instance.getDataFolder();
            if (dataFolder == null) throw new IllegalStateException();
            file = new File(dataFolder, fileName);
        }
        fileConfig = YamlConfiguration.loadConfiguration(file);

        // Look for non-existant defaults
        InputStream defConfigStream = BattleNight.instance.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfig.setDefaults(defConfig);
            fileConfig.options().indent(4);
            fileConfig.options().copyDefaults(true);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfig == null) {
            this.reload();
        }
        return fileConfig;
    }

    public void save() {
        if (fileConfig == null || file == null) {
            return;
        } else {
            try {
                getConfig().save(file);
            } catch (IOException ex) {
                BattleNight.log.severe("Could not save config to " + file + ": " + ex.getMessage());
            }
        }
    }
}
