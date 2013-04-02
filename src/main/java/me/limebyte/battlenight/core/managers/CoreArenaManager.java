package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.core.battle.SimpleWaypoint;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

public class CoreArenaManager implements ArenaManager {

    private BattleNightAPI api;
    private List<Arena> arenas = new ArrayList<Arena>();
    private static final Config configFile = Config.ARENAS;

    private SimpleWaypoint lounge = new SimpleWaypoint();
    private SimpleWaypoint exit = new SimpleWaypoint();

    private static Arena lastArena;
    private static Random random = new Random();

    public CoreArenaManager(BattleNightAPI api) {
        this.api = api;
        loadArenas();
    }

    @Override
    public void deregister(Arena arena) {
        arenas.remove(arena);
    }

    @Override
    public List<Arena> getArenas() {
        return arenas;
    }

    @Override
    public List<Arena> getEnabledArenas() {
        List<Arena> enabled = new ArrayList<Arena>(arenas);
        Iterator<Arena> it = enabled.iterator();
        while (it.hasNext()) {
            Arena arena = it.next();
            if (!arena.isEnabled()) {
                it.remove();
            }
        }
        return enabled;
    }

    @Override
    public SimpleWaypoint getExit() {
        return exit;
    }

    @Override
    public SimpleWaypoint getLounge() {
        return lounge;
    }

    @Override
    public Arena getRandomArena(int minSpawns) {
        List<Arena> ready = getReadyArenas(minSpawns);
        if (ready.size() > 1) {
            ready.remove(lastArena);
        }

        Arena arena = ready.get(random.nextInt(ready.size()));
        lastArena = arena;
        return arena;
    }

    @Override
    public List<Arena> getReadyArenas(int minSpawns) {
        List<Arena> ready = new ArrayList<Arena>(arenas);
        Iterator<Arena> it = ready.iterator();
        while (it.hasNext()) {
            Arena arena = it.next();
            if (!arena.isSetup(minSpawns) || !arena.isEnabled()) {
                it.remove();
            }
        }
        return ready;
    }

    @Override
    public List<Arena> getSetupArenas(int minSpawns) {
        List<Arena> setup = new ArrayList<Arena>(arenas);
        Iterator<Arena> it = setup.iterator();
        while (it.hasNext()) {
            Arena arena = it.next();
            if (!arena.isSetup(minSpawns)) {
                it.remove();
            }
        }
        return setup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadArenas() {
        api.getMessenger().debug(Level.INFO, "Loading arenas...");
        ConfigManager.reload(configFile);

        lounge = (SimpleWaypoint) ConfigManager.get(configFile).get("Waypoint.Lounge", lounge);
        exit = (SimpleWaypoint) ConfigManager.get(configFile).get("Waypoint.Exit", exit);
        arenas = (List<Arena>) ConfigManager.get(configFile).getList("Arenas", arenas);
    }

    @Override
    public void register(Arena arena) {
        arenas.add(arena);
    }

    @Override
    public void saveArenas() {
        api.getMessenger().debug(Level.INFO, "Saving arenas...");
        ConfigManager.get(configFile).set("Waypoint.Lounge", lounge);
        ConfigManager.get(configFile).set("Waypoint.Exit", exit);
        ConfigManager.get(configFile).set("Arenas", arenas);
        ConfigManager.save(configFile);
    }

}
