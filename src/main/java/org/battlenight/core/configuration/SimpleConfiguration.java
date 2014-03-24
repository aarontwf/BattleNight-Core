package org.battlenight.core.configuration;

import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.configuration.Configuration;
import org.battlenight.core.configuration.file.Config;
import org.battlenight.core.configuration.file.DataConfig;
import org.battlenight.core.configuration.file.MessageConfig;
import org.battlenight.core.configuration.file.StandardConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class SimpleConfiguration implements Configuration {

    // Standard Configurations
    private Config options;
    private Config loadouts;

    // Storage Configurations
    private Config locations;
    private Config maps;

    // Message Configurations
    private Config english;
    private Config custom;

    public SimpleConfiguration(Plugin plugin) {
        options = new StandardConfig(plugin, ConfigFile.OPTIONS.getName(), true);
        loadouts = new StandardConfig(plugin, ConfigFile.LOADOUTS.getName(), false);

        locations = new DataConfig(plugin, ConfigFile.LOCATIONS.getName());
        maps = new DataConfig(plugin, ConfigFile.MAPS.getName());

        english = new MessageConfig(plugin, ConfigFile.MSG_ENGLISH.getName(), true);
        custom = new MessageConfig(plugin, ConfigFile.MSG_CUSTOM.getName(), false);
    }

    public FileConfiguration get(ConfigFile file) {
        Config config = getByFile(file);
        return config != null ? config.getConfig() : null;
    }

    @Override
    public void reload() {
        for (ConfigFile file : ConfigFile.values()) {
            reload(file);
        }
    }

    @Override
    public void reload(ConfigFile file) {
        Config config = getByFile(file);
        if (config != null) config.reload();
    }

    @Override
    public void save(ConfigFile file) {
        Config config = getByFile(file);
        if (config != null) config.save();
    }

    private Config getByFile(ConfigFile file) {
        switch (file) {
            case OPTIONS:
                return options;
            case LOADOUTS:
                return loadouts;
            case LOCATIONS:
                return locations;
            case MAPS:
                return maps;
            case MSG_ENGLISH:
                return english;
            case MSG_CUSTOM:
                return custom;
            default:
                return null;
        }
    }

}
