package me.limebyte.battlenight.core.Battle;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
public class Arena {
    Waypoints waypoints = new Waypoints(this);
    String name;

    public Arena(String arenaName) {
        name = arenaName;
    }

    /**
     * Get the name of the Arena
     * @return Name of the Arena
     */
    public String getName() {
        return name;
    }

    /**
     * Get the waypoints for the arena
     * @return Waypoints
     */
    public Waypoints getWaypoints() {
        return waypoints;
    }

    /**
     * Checks if all waypoints are set
     * @return True if setup
     */
    public boolean isSetup() {
        for(Waypoints.Waypoint w : Waypoints.Waypoint.values()) {
            if(!waypoints.isSet(w)) return false;
        }
        return true;
    }
}
