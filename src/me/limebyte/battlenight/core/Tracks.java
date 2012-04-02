package me.limebyte.battlenight.core;

import me.limebyte.battlenight.core.Configuration.Config;
import org.bukkit.ChatColor;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
public class Tracks {

    // Get Main Class
    public static BattleNight plugin;
    public Tracks(BattleNight instance) {
        plugin = instance;
    }
    
    private static final String prefix = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;

    public enum Track {
        ;

        private Track(String configPath) {
            this.cp = configPath;
        }
        private String cp;

        public String getMessage() {
            return prefix + plugin.config.get(Config.ConfigFile.TRACKS).getString(cp).replaceAll("(&([a-f0-9]))", "\u00A7$2");
        }
    }
}
