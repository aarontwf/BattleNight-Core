package me.limebyte.battlenight.api.battle;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface Waypoint extends ConfigurationSerializable {

    public Location getLocation();

    public boolean isSet();

    public void setLocation(Location location);

}
