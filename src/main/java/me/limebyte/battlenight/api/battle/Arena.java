package me.limebyte.battlenight.api.battle;

import java.util.ArrayList;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface Arena extends ConfigurationSerializable {

    public int addSpawnPoint(Waypoint waypoint);

    public void disable();

    public void enable();

    public String getDisplayName();

    public String getName();

    public Waypoint getRandomSpawnPoint();

    public ArrayList<Waypoint> getSpawnPoints();

    public String getTexturePack();

    public boolean isEnabled();

    public boolean isSetup(int minSpawnPoints);

    public void removeSpawnPoint(Waypoint waypoint);

    public void setDisplayName(String displayName);

    public void setEnabled(boolean enabled);

    public void setTexturePack(String texturePack);

}
