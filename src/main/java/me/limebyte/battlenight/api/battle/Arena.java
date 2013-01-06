package me.limebyte.battlenight.api.battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Arena implements ConfigurationSerializable {

    private String name;
    private String displayName;
    private Set<Waypoint> spawnPoints = new HashSet<Waypoint>();
    private boolean enabled = true;

    public Arena(String name) {
        this.name = name.toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public Arena(Map<String, Object> map) {
        name = (String) map.get("Name");
        displayName = (String) map.get("DisplayName");
        spawnPoints = (Set<Waypoint>) map.get("SpawnPoints");
        enabled = (Boolean) map.get("Enabled");
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
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSetup(int minSpawnPoints) {
        return spawnPoints.size() >= minSpawnPoints;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Name", name);
        map.put("DisplayName", displayName);
        map.put("SpawnPoints", spawnPoints);
        map.put("Enabled", enabled);
        return map;
    }

}
