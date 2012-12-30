package me.limebyte.battlenight.core.battle;

import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public enum Waypoint {
    RED_LOUNGE("redlounge", "Red Lounge"),
    BLUE_LOUNGE("bluelounge", "Blue Lounge"),
    RED_SPAWN("redspawn", "Red Spawn"),
    BLUE_SPAWN("bluespawn", "Blue Spawn"),
    SPECTATOR("spectator", "Spectator"),
    EXIT("exit", "Exit");

    private String name;
    private String displayName;

    Waypoint(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location getLocation() {
        FileConfiguration config = ConfigManager.get(Config.ARENAS);
        return me.limebyte.battlenight.api.Waypoint.parseLocation(config.getString("default." + name));
    }

    public void setLocation(Location loc) {
        ConfigManager.reload(Config.ARENAS);
        FileConfiguration config = ConfigManager.get(Config.ARENAS);
        config.set("default." + name, me.limebyte.battlenight.api.Waypoint.parseLocation(loc));
        ConfigManager.save(Config.ARENAS);
    }

    public boolean isSet() {
        FileConfiguration config = ConfigManager.get(Config.ARENAS);
        return config.getString("default." + name) != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
