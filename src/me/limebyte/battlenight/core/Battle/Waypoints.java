package me.limebyte.battlenight.core.Battle;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Configuration.Config;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
public class Waypoints {

    Arena arena;
    Config.ConfigFile file = Config.ConfigFile.WAYPOINTS;

    // Get Main Class
    public static BattleNight plugin;

    public Waypoints(BattleNight instance) {
        plugin = instance;
    }

    Waypoints(Arena a) {
        arena = a;
    }

    public void setLocation(Waypoint wp, Location l) {
        String location = l.getWorld().getName() + "|"
                + l.getX() + "|"
                + l.getY() + "|"
                + l.getZ() + "|"
                + l.getYaw() + "|"
                + l.getPitch();
        plugin.config.get(file).set(arena.getName()+"."+wp.getName(wp), location);
        plugin.config.save(file);
    }

    public Location getLocation(Waypoint wp) {
        plugin.config.reload(file);
        String location = plugin.config.get(file).getString(arena.getName()+"."+wp.getName(wp));
        String[] locationParts = location.split("|");
        World world = plugin.getServer().getWorld(locationParts[0]);
        double x = Double.valueOf(locationParts[1]);
        double y = Double.valueOf(locationParts[2]);
        double z = Double.valueOf(locationParts[3]);
        float yaw = Float.valueOf(locationParts[4]);
        float pitch = Float.valueOf(locationParts[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public boolean isSet(Waypoint wp) {
        return getLocation(wp) != null;
    }

    public enum Waypoint {
        BLUELOUNGE,
        REDLOUNGE,
        BLUESPAWN,
        REDSPAWN;
        
        public String getName(Waypoint wp) {
            return wp.toString().toLowerCase();
        }
    }
}

