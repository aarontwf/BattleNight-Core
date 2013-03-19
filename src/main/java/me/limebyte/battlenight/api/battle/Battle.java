package me.limebyte.battlenight.api.battle;

import java.util.Set;

import me.limebyte.battlenight.api.util.Timer;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Battle {

    public void addDeath(Player player);

    public void addKill(Player player);

    /**
     * Adds the specified {@link Player} to the battle. This will return false
     * if it is unsuccessful.
     * 
     * @param player the Player to add
     * @return true if successful
     */
    public boolean addPlayer(Player player);

    public boolean containsPlayer(Player player);

    /**
     * Returns the {@link Arena} that is set for this battle.
     * 
     * @return the arena
     * @see Arena
     */
    public Arena getArena();

    public int getDeaths(Player player);

    public double getKDR(Player player);

    public int getKills(Player player);

    public Set<String> getLeadingPlayers();

    /**
     * Returns the maximum amount of players the battle can have. By default
     * this is set to {@link Integer.MAX_VALUE}.
     * 
     * @return the maxPlayers
     */
    public int getMaxPlayers();

    /**
     * Returns the minimum amount of players the battle requires before it can
     * be started.
     * 
     * @return the minPlayers
     */
    public int getMinPlayers();

    /**
     * @return the players
     */
    public Set<String> getPlayers();

    public Timer getTimer();

    /**
     * @return if in progress
     */
    public boolean isInProgress();

    public boolean onStart();

    public boolean onStop();

    /**
     * Removes the specified {@link Player} to the battle. This will return
     * false if it is unsuccessful.
     * 
     * @param player the Player to remove
     * @return true if successful
     */
    public boolean removePlayer(Player player);

    public Location respawn(Player player);

    /**
     * Sets the {@link Arena} that will be used for this battle. The arena will
     * not be set if this battle is in progress.
     * 
     * @param arena the arena to set
     * @see Arena
     */
    public void setArena(Arena arena);

    /**
     * Sets the maximum amount of players the battle can have. Setting this
     * value will prevent players from joining if the battle is full. This
     * cannot be set to a value that is less than the minimum.
     * 
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxPlayers);

    /**
     * Sets the minimum amount of players the battle requires before it can be
     * started. This cannot be set below one.
     * 
     * @param minPlayers the minPlayers to set
     */
    public void setMinPlayers(int minPlayers);

    public boolean start();

    public boolean stop();

}
