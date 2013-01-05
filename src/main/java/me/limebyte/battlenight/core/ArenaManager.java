package me.limebyte.battlenight.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Util;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

public class ArenaManager {

    private static List<Arena> arenas = new ArrayList<Arena>();
    private static final Config configFile = Config.ARENAS;

    private ArenaManager() {
        // Private constructor for utility class
    }

    public static void loadArenas() {
        Messenger.debug(Level.INFO, "Loading arenas...");
        ConfigManager.reload(configFile);
        for (String arenaName : ConfigManager.get(configFile).getConfigurationSection("Arenas").getKeys(false)) {
            Arena arena = new Arena(arenaName);
            arena.setDisplayName(ConfigManager.get(configFile).getString("Arenas." + arenaName + ".DisplayName"));
            arena.setEnabled(ConfigManager.get(configFile).getBoolean("Arenas." + arenaName + ".Enabled"));
            register(arena);
        }
    }

    public static void saveArenas() {
        Messenger.debug(Level.INFO, "Saving arenas...");
        for (Arena a : arenas) {
            ConfigManager.get(configFile).set("Arenas." + a.getName() + ".DisplayName", a.getDisplayName());
            ConfigManager.get(configFile).set("Arenas." + a.getName() + ".Enabled", a.isEnabled());
        }
        ConfigManager.save(configFile);
    }

    public static void register(Arena arena) {
        arenas.add(arena);
    }

    public static void unregister(Arena arena) {
        arenas.remove(arena);
    }

    public static List<Arena> getArenas() {
        return arenas;
    }

    public static Arena getRandomArena() {
        return (Arena) Util.getRandom(arenas);
    }

}
