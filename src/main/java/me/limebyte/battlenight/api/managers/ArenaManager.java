package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.api.tosort.Arena;
import me.limebyte.battlenight.api.tosort.Waypoint;

public interface ArenaManager {

    /**
     * Loads the arenas from the configuration file into the manager.
     */
    public void loadArenas();

    /**
     * Saves the arenas back to the configuration file.
     */
    public void saveArenas();

    /**
     * Registers a new arena in the manager.
     * 
     * @param arena The arena to register.
     */
    public void register(Arena arena);

    /**
     * DeRegisters an arena from the manager effectively removing it.
     * 
     * @param arena The arena to remove.
     */
    public void deregister(Arena arena);

    /**
     * Gets the loaded arenas.
     * 
     * @return loaded arenas.
     */
    public List<Arena> getArenas();

    /**
     * Gets the loaded and enabled arenas.
     * 
     * @return enabled arenas.
     */
    public List<Arena> getEnabledArenas();

    /**
     * Gets the loaded and setup arenas with at least the specified amount of
     * spawn points.
     * 
     * @param minSpawns The minimum amount of spawn points.
     * @return setup arenas.
     */
    public List<Arena> getSetupArenas(int minSpawns);

    /**
     * Gets the loaded, enabled and setup arenas with at least the specified
     * amount of spawn points.
     * 
     * @param minSpawns The minimum amount of spawn points.
     * @return ready arenas.
     */
    public List<Arena> getReadyArenas(int minSpawns);

    /**
     * Gets a random loaded arena.
     * 
     * @return a random arena.
     */
    public Arena getRandomArena(int minSpawns);

    /**
     * Gets the lounge waypoint.
     * 
     * @return lounge waypoint.
     */
    public Waypoint getLounge();

    /**
     * Gets the exit waypoint.
     * 
     * @return exit waypoint.
     */
    public Waypoint getExit();

}
