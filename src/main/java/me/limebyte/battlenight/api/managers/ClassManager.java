package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.api.util.PlayerClass;

public interface ClassManager {

    /**
     * Gets the loaded classes.
     * 
     * @return loaded classes.
     */
    public List<PlayerClass> getClasses();

    /**
     * Gets a random loaded class.
     * 
     * @return a random class.
     */
    public PlayerClass getRandomClass();

    /**
     * Loads the classes from the configuration file into the manager.
     */
    public void loadClasses();

    /**
     * Loads then saves the classes from/to the configuration file.
     */
    public void reloadClasses();

    /**
     * Saves the classes back to the configuration file.
     */
    public void saveClasses();

}
