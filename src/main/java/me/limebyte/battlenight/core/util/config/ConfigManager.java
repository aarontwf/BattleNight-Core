package me.limebyte.battlenight.core.util.config;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private static Configuration mainConfig = new Configuration("config.yml");
    private static Configuration classesConfig = new Configuration("classes.yml");
    private static Configuration waypointsConfig = new Configuration("waypoints.dat", ".plugindata");

    public static void initConfigurations() {
        reloadAll();
        saveAll();
    }

    public static FileConfiguration get(Config configFile) {
        return configFile.getConfiguration().get();
    }

    public static void reload(Config configFile) {
        configFile.getConfiguration().reload();
    }

    public static void save(Config configFile) {
        configFile.getConfiguration().save();
    }

    public static void reloadAll() {
        for (Config configFile : Config.values()) {
            configFile.getConfiguration().reload();
        }
    }

    public static void saveAll() {
        for (Config configFile : Config.values()) {
            configFile.getConfiguration().save();
        }
    }

    public enum Config {
        MAIN(mainConfig),
        CLASSES(classesConfig),
        WAYPOINTS(waypointsConfig);

        private Configuration config;

        Config(Configuration config) {
            this.config = config;
        }

        public Configuration getConfiguration() {
            return config;
        }
    }

}
