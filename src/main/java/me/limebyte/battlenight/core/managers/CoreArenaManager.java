package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

public class CoreArenaManager implements ArenaManager {

    private List<Arena> arenas = new ArrayList<Arena>();
    private static final Config configFile = Config.ARENAS;

    private Waypoint lounge = new Waypoint();
    private Waypoint exit = new Waypoint();

    private final Random RANDOM = new Random();

    public CoreArenaManager() {
        loadArenas();
    }

    @SuppressWarnings("unchecked")
    public void loadArenas() {
        Messenger.debug(Level.INFO, "Loading arenas...");
        ConfigManager.reload(configFile);

        lounge = (Waypoint) ConfigManager.get(configFile).get("Waypoint;Lounge", lounge);
        exit = (Waypoint) ConfigManager.get(configFile).get("Waypoint;Exit", exit);
        arenas = (List<Arena>) ConfigManager.get(configFile).getList("Arenas", arenas);
    }

    public void saveArenas() {
        Messenger.debug(Level.INFO, "Saving arenas...");
        ConfigManager.get(configFile).set("Waypoint;Lounge", lounge);
        ConfigManager.get(configFile).set("Waypoint;Exit", exit);
        ConfigManager.get(configFile).set("Arenas", arenas);
        ConfigManager.save(configFile);
    }

    public void register(Arena arena) {
        arenas.add(arena);
    }

    public void deregister(Arena arena) {
        arenas.remove(arena);
    }

    public List<Arena> getArenas() {
        return arenas;
    }

    public List<Arena> getEnabledArenas() {
        List<Arena> enabled = new ArrayList<Arena>();
        for (Arena arena : arenas) {
            if (arena.isEnabled()) enabled.add(arena);
        }
        return enabled;
    }

    public Arena getRandomArena() {
        List<Arena> enabled = getEnabledArenas();
        return (enabled.get(RANDOM.nextInt(enabled.size())));
    }

    public Waypoint getLounge() {
        return lounge;
    }

    public Waypoint getExit() {
        return exit;
    }

}
