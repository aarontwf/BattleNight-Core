package org.battlenight.core.configuration.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {

    protected final Plugin plugin;
    protected final String fileName;
    protected final String directory;
    protected final boolean copyDefaults;
    protected final boolean overwrite;

    protected File configFile;
    protected YamlConfiguration config;

    public Config(Plugin plugin, String fileName, boolean copyDefaults, boolean overwrite) {
        this.fileName = fileName;
        this.directory = plugin.getDataFolder().getAbsolutePath();
        this.copyDefaults = copyDefaults;
        this.plugin = plugin;
        this.overwrite = overwrite;
    }

    public Config(Plugin plugin, String fileName, String directory, boolean copyDefaults, boolean overwrite) {
        this.fileName = fileName;
        this.directory = plugin.getDataFolder().getAbsolutePath() + File.separator + directory;
        this.copyDefaults = copyDefaults;
        this.plugin = plugin;
        this.overwrite = overwrite;
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reload();
        }
        return config;
    }

    public void reload() {
        if (configFile == null) {
            configFile = new File(directory, fileName);
        }

        saveResource();
        config = YamlConfiguration.loadConfiguration(configFile);
        copyDefaults();
    }

    public void save() {
        if (config == null || configFile == null) return;
        else {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
            }
        }
    }

    protected void copyDefaults() {
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.options().indent(4);
            if (copyDefaults) {
                config.setDefaults(defConfig);
            }
        }
    }

    private void saveResource() {
        String resourcePath = fileName.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in the jar.");
        }

        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(directory, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!configFile.exists() || overwrite) {
                OutputStream out = new FileOutputStream(configFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ex) {
        }
    }

}
