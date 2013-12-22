package org.battlenight.core.configuration.file;

import org.bukkit.plugin.Plugin;

public class StandardConfig extends Config {

    public StandardConfig(Plugin plugin, String fileName, boolean copyDefaults) {
        super(plugin, fileName + ".yml", copyDefaults, false);
    }

}
