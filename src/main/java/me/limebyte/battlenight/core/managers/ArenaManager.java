package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.battle.Waypoint.WaypointType;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Util;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

public class ArenaManager {

    private static List<Arena> arenas;
    private static final Config configFile = Config.ARENAS;

    private static Waypoint lounge;
    private static Waypoint exit;

    private ArenaManager() {
        // Private constructor for utility class
    }

    @SuppressWarnings("unchecked")
    public static void loadArenas() {
        Messenger.debug(Level.INFO, "Loading arenas...");
        ConfigManager.reload(configFile);

        lounge = (Waypoint) ConfigManager.get(configFile).get("Waypoint.Lounge", new Waypoint(WaypointType.LOUNGE));
        exit = (Waypoint) ConfigManager.get(configFile).get("Waypoint.Exit", new Waypoint(WaypointType.EXIT));
        arenas = (List<Arena>) ConfigManager.get(configFile).getList("Arenas", new ArrayList<Arena>());
    }

    public static void saveArenas() {
        Messenger.debug(Level.INFO, "Saving arenas...");
        ConfigManager.get(configFile).set("Lounge", lounge);
        ConfigManager.get(configFile).set("Exit", exit);
        ConfigManager.get(configFile).set("Arenas", arenas);
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

    public static Waypoint getLounge() {
        return lounge;
    }

    public static Waypoint getExit() {
        return exit;
    }

}
