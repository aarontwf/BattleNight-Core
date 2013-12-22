package org.battlenight.core.configuration.file;

import org.bukkit.plugin.Plugin;

public class DataConfig extends Config {

    public DataConfig(Plugin plugin, String fileName) {
        super(plugin, fileName + ".dat", "data", false, false);
    }

}
