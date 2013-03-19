package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.core.battle.SimpleArena;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;
import me.limebyte.battlenight.core.tosort.Messenger;
import me.limebyte.battlenight.core.tosort.Waypoint;

public class CoreArenaManager implements ArenaManager {

    private List<SimpleArena> arenas = new ArrayList<SimpleArena>();
    private static final Config configFile = Config.ARENAS;

    private Waypoint lounge = new Waypoint();
    private Waypoint exit = new Waypoint();

    private static SimpleArena lastArena;
    private static Random random = new Random();

    public CoreArenaManager() {
        loadArenas();
    }

    @Override
    public void deregister(SimpleArena arena) {
        arenas.remove(arena);
    }

    @Override
    public List<SimpleArena> getArenas() {
        return arenas;
    }

    @Override
    public List<SimpleArena> getEnabledArenas() {
        List<SimpleArena> enabled = new ArrayList<SimpleArena>(arenas);
        Iterator<SimpleArena> it = enabled.iterator();
        while (it.hasNext()) {
            SimpleArena arena = it.next();
            if (!arena.isEnabled()) {
                it.remove();
            }
        }
        return enabled;
    }

    @Override
    public Waypoint getExit() {
        return exit;
    }

    @Override
    public Waypoint getLounge() {
        return lounge;
    }

    @Override
    public SimpleArena getRandomArena(int minSpawns) {
        List<SimpleArena> ready = getReadyArenas(minSpawns);
        if (ready.size() > 1) {
            ready.remove(lastArena);
        }

        SimpleArena arena = ready.get(random.nextInt(ready.size()));
        lastArena = arena;
        return arena;
    }

    @Override
    public List<SimpleArena> getReadyArenas(int minSpawns) {
        List<SimpleArena> ready = new ArrayList<SimpleArena>(arenas);
        Iterator<SimpleArena> it = ready.iterator();
        while (it.hasNext()) {
            SimpleArena arena = it.next();
            if (!arena.isSetup(minSpawns) || !arena.isEnabled()) {
                it.remove();
            }
        }
        return ready;
    }

    @Override
    public List<SimpleArena> getSetupArenas(int minSpawns) {
        List<SimpleArena> setup = new ArrayList<SimpleArena>(arenas);
        Iterator<SimpleArena> it = setup.iterator();
        while (it.hasNext()) {
            SimpleArena arena = it.next();
            if (!arena.isSetup(minSpawns)) {
                it.remove();
            }
        }
        return setup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadArenas() {
        Messenger.debug(Level.INFO, "Loading arenas...");
        ConfigManager.reload(configFile);

        lounge = (Waypoint) ConfigManager.get(configFile).get("Waypoint.Lounge", lounge);
        exit = (Waypoint) ConfigManager.get(configFile).get("Waypoint.Exit", exit);
        arenas = (List<SimpleArena>) ConfigManager.get(configFile).getList("Arenas", arenas);
    }

    @Override
    public void register(SimpleArena arena) {
        arenas.add(arena);
    }

    @Override
    public void saveArenas() {
        Messenger.debug(Level.INFO, "Saving arenas...");
        ConfigManager.get(configFile).set("Waypoint.Lounge", lounge);
        ConfigManager.get(configFile).set("Waypoint.Exit", exit);
        ConfigManager.get(configFile).set("Arenas", arenas);
        ConfigManager.save(configFile);
    }

}
