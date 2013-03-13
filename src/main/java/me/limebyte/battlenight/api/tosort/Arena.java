package me.limebyte.battlenight.api.tosort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Arena")
public class Arena implements ConfigurationSerializable {

    @SuppressWarnings("unchecked")
    public static Arena deserialize(Map<String, Object> map) {
        Object displayName = map.get("DisplayName");
        Object spawnPoints = map.get("SpawnPoints");
        Object enabled = map.get("Enabled");
        Object texturePack = map.get("TexturePack");

        Arena arena = new Arena((String) map.get("Name"));
        if (displayName != null) {
            arena.displayName = (String) displayName;
        }
        if (spawnPoints != null) {
            arena.spawnPoints = (ArrayList<Waypoint>) spawnPoints;
        }
        if (enabled != null) {
            arena.enabled = (Boolean) enabled;
        }
        if (texturePack != null) {
            arena.texturePack = (String) texturePack;
        }

        return arena;
    }

    private String name;
    private String displayName;
    private ArrayList<Waypoint> spawnPoints = new ArrayList<Waypoint>();
    private boolean enabled = true;
    private String texturePack = "";

    private final Random RANDOM = new Random();

    public Arena(String name) {
        this.name = name.toLowerCase();
        displayName = name;
    }

    public int addSpawnPoint(Waypoint waypoint) {
        spawnPoints.add(waypoint);
        return spawnPoints.size() - 1;
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public Waypoint getRandomSpawnPoint() {
        return getSpawnPoints().get(RANDOM.nextInt(getSpawnPoints().size()));
    }

    public ArrayList<Waypoint> getSpawnPoints() {
        return spawnPoints;
    }

    public String getTexturePack() {
        return texturePack;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSetup(int minSpawnPoints) {
        return spawnPoints.size() >= minSpawnPoints;
    }

    public void removeSpawnPoint(Waypoint waypoint) {
        spawnPoints.remove(waypoint);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Name", name);
        map.put("DisplayName", displayName);
        map.put("SpawnPoints", spawnPoints);
        map.put("Enabled", enabled);
        map.put("TexturePack", texturePack);
        return map;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setTexturePack(String texturePack) {
        this.texturePack = texturePack;
    }

    @Override
    public String toString() {
        return name;
    }
}
