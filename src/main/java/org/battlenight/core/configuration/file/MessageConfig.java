package org.battlenight.core.configuration.file;

import java.io.InputStream;

import org.battlenight.api.configuration.ConfigFile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class MessageConfig extends Config {

    public MessageConfig(Plugin plugin, String language, boolean overwrite) {
        super(plugin, language + ".yml", "messages", true, overwrite);
    }

    @Override
    protected void copyDefaults() {
        InputStream defConfigStream = plugin.getResource(ConfigFile.MSG_ENGLISH.getName() + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.options().indent(4);
            config.setDefaults(defConfig);
        }
    }

}
