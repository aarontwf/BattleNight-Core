package me.limebyte.battlenight.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Util {

    public void preparePlayer(Player player, Location dest);

    public void restorePlayer(Player player);

    public String parseLocation(Location loc);

    public Location parseLocation(String string);

}
