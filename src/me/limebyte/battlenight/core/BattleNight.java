package me.limebyte.battlenight.core;

import me.limebyte.battlenight.core.Configuration.Config;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */

public class BattleNight extends JavaPlugin {

    public PluginDescriptionFile pdFile = this.getDescription();
    public Logger log = this.getLogger();
    public Config config;

    @Override
    public void onEnable() {

        // Reload configuration files
        config.reload(Config.ConfigFile.MAIN);
        config.reload(Config.ConfigFile.CLASSES);
        config.reload(Config.ConfigFile.WAYPOINTS);
        config.reload(Config.ConfigFile.PLAYERS);

        // Load command class
        Commands cmdExecutor = new Commands(this);
        getCommand("bn").setExecutor(cmdExecutor);

        // Print enable message to the console
        log.info("Version " + pdFile.getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        // Print disable message to the console
        log.info("Version " + pdFile.getVersion() + " disabled.");
    }

}
