package me.limebyte.battlenight.api.managers;

import me.limebyte.battlenight.api.util.Song;

public interface MusicManager {

    /**
     * Returns a new {@link Song} from the music folder with the specified name.
     * The file extension must be ".nbs" and not included in the parameter. This
     * will return null if it is not found.
     * 
     * @param name The file name to search for.
     * @return the song.
     */
    public Song load(String name);

}
