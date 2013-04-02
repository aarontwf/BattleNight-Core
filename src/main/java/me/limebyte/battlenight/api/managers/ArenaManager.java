package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.core.battle.SimpleArena;

public interface ArenaManager {

    /**
     * DeRegisters an arena from the manager effectively removing it.
     * 
     * @param arena The arena to remove.
     */
    public void deregister(SimpleArena arena);

    /**
     * Gets the loaded arenas.
     * 
     * @return loaded arenas.
     */
    public List<SimpleArena> getArenas();

    /**
     * Gets the loaded and enabled arenas.
     * 
     * @return enabled arenas.
     */
    public List<SimpleArena> getEnabledArenas();

    /**
     * Gets the exit waypoint.
     * 
     * @return exit waypoint.
     */
    public Waypoint getExit();

    /**
     * Gets the lounge waypoint.
     * 
     * @return lounge waypoint.
     */
    public Waypoint getLounge();

    /**
     * Gets a random loaded arena.
     * 
     * @return a random arena.
     */
    public SimpleArena getRandomArena(int minSpawns);

    /**
     * Gets the loaded, enabled and setup arenas with at least the specified
     * amount of spawn points.
     * 
     * @param minSpawns The minimum amount of spawn points.
     * @return ready arenas.
     */
    public List<SimpleArena> getReadyArenas(int minSpawns);

    /**
     * Gets the loaded and setup arenas with at least the specified amount of
     * spawn points.
     * 
     * @param minSpawns The minimum amount of spawn points.
     * @return setup arenas.
     */
    public List<SimpleArena> getSetupArenas(int minSpawns);

    /**
     * Loads the arenas from the configuration file into the manager.
     */
    public void loadArenas();

    /**
     * Registers a new arena in the manager.
     * 
     * @param arena The arena to register.
     */
    public void register(SimpleArena arena);

    /**
     * Saves the arenas back to the configuration file.
     */
    public void saveArenas();

}
