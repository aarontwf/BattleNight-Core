package me.limebyte.battlenight.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public interface Util {

    public void preparePlayer(Player player, Location dest);

    public String parseLocation(Location loc);

    public Location parseLocation(String string);

    public boolean inventoryEmpty(PlayerInventory inv);

}
