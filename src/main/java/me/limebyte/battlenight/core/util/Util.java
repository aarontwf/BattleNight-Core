package me.limebyte.battlenight.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Util {

    ////////////////////
    //   Locations    //
    ////////////////////

    public static String locationToString(Location loc) {
        String w = loc.getWorld().getName();
        double x = loc.getBlockX() + 0.5;
        double y = loc.getBlockY();
        double z = loc.getBlockZ() + 0.5;
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return w + "," + x + "," + y + "," + z + "," + yaw + "," + pitch;
    }

    public static Location locationFromString(String s) {
        String part[] = s.split(",");
        World w = Bukkit.getServer().getWorld(part[0]);
        double x = Double.parseDouble(part[1]);
        double y = Double.parseDouble(part[2]);
        double z = Double.parseDouble(part[3]);
        float yaw = Float.parseFloat(part[4]);
        float pitch = Float.parseFloat(part[5]);
        return new Location(w, x, y, z, yaw, pitch);
    }

    ////////////////////
    //     Items      //
    ////////////////////

    public static void clearInventory(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[inv.getArmorContents().length]);
    }
}
