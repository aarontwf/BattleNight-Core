package me.limebyte.battlenight.api;

import java.util.Set;

public interface Arena {

    public String getName();

    public String getDisplayName();

    public void setDisplayName();

    public Set<Waypoint> getWaypoints();

    public void addWaypoint(Waypoint waypoint);

    public void removeWaypoint(Waypoint waypoint);

    public void enable();

    public void disable();

    public boolean isEnabled();

    public boolean isSetup();

}
