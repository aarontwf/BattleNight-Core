package me.limebyte.battlenight.api.battle;

import java.util.Set;

public class Arena {

    private String name;
    private String displayName;
    private Set<Waypoint> spawnPoints;
    private boolean enabled = true;

    public Arena(String name) {
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<Waypoint> getSpawnPoints() {
        return spawnPoints;
    }

    public void addSpawnPoint(Waypoint waypoint) {
        spawnPoints.add(waypoint);
    }

    public void removeSpawnPoint(Waypoint waypoint) {
        spawnPoints.remove(waypoint);
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSetup(int minSpawnPoints) {
        return spawnPoints.size() >= minSpawnPoints;
    }

}
