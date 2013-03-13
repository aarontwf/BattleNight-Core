package me.limebyte.battlenight.api.util;

import org.bukkit.entity.Player;

public interface Song {

    /**
     * Returns true if the specified player is listening to this song.
     * 
     * @param player
     * @return true if listening
     */
    public boolean isListening(Player player);

    /**
     * Returns the length of this song in ticks.
     * 
     * @return length in ticks.
     */
    public long length();

    /**
     * Plays the song to the specified player.
     * 
     * @param player
     */
    public void play(Player player);

    /**
     * Stops the song playing the specified player.
     * 
     * @param player
     */
    public void stop(Player player);

}
