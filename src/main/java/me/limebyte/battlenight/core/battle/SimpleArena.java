package me.limebyte.battlenight.core.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.core.tosort.Waypoint;

import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Arena")
public class SimpleArena implements Arena {

    @SuppressWarnings("unchecked")
    public static SimpleArena deserialize(Map<String, Object> map) {
        Object displayName = map.get("DisplayName");
        Object spawnPoints = map.get("SpawnPoints");
        Object enabled = map.get("Enabled");
        Object texturePack = map.get("TexturePack");

        SimpleArena arena = new SimpleArena((String) map.get("Name"));
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

    public SimpleArena(String name) {
        this.name = name.toLowerCase();
        displayName = name;
    }

    @Override
    public int addSpawnPoint(Waypoint waypoint) {
        spawnPoints.add(waypoint);
        return spawnPoints.size() - 1;
    }

    @Override
    public void disable() {
        setEnabled(false);
    }

    @Override
    public void enable() {
        setEnabled(true);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Waypoint getRandomSpawnPoint() {
        return getSpawnPoints().get(RANDOM.nextInt(getSpawnPoints().size()));
    }

    @Override
    public ArrayList<Waypoint> getSpawnPoints() {
        return spawnPoints;
    }

    @Override
    public String getTexturePack() {
        return texturePack;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isSetup(int minSpawnPoints) {
        return spawnPoints.size() >= minSpawnPoints;
    }

    @Override
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

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setTexturePack(String texturePack) {
        this.texturePack = texturePack;
    }

    @Override
    public String toString() {
        return name;
    }
}
