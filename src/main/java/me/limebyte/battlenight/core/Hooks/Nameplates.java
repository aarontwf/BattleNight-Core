package me.limebyte.battlenight.core.Hooks;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import me.limebyte.battlenight.core.BattleNight;

public class Nameplates {
    public static boolean init(BattleNight plugin) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        if (pm.getPlugin("TagAPI") == null) {
            BattleNight.log.info("TagAPI not found.  Installing...");
            try {
                install(plugin, pm);
            } catch (Exception e) {
                BattleNight.log.info("Failed to install TagAPI.  Disabling...");
                return false;
            } finally {
                BattleNight.log.info("TagAPI installed successfully.");
            }
        }
        return true;
    }
    
    private static void install(BattleNight plugin, PluginManager pm) throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException {
        File tagAPI = new File(plugin.getDataFolder().getParent(), "TagAPI.jar");
        BattleNight.copy(plugin.getResource("TagAPI.jar"), tagAPI);
        load(tagAPI);
    }
    
    private static void load(File file) throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.loadPlugin(file);
        pm.enablePlugin(pm.getPlugin("TagAPI"));
    }
}
