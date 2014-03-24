package org.battlenight.core.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.battlenight.api.game.GameTeam;
import org.battlenight.api.map.GameMap;
import org.bukkit.Location;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SimpleMap implements GameMap {

    private String name;
    private String displayName;
    private Map<String, GameTeam> teams;
    private Map<String, List<Location>> spawns;
    private Map<String, Location> objectives;

    private Random random;

    public SimpleMap() {
        this.teams = Maps.newHashMap();
        this.spawns = Maps.newHashMap();
        this.objectives = Maps.newHashMap();

        this.random = new Random();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    @Override
    public List<GameTeam> getTeams() {
        return new ArrayList<GameTeam>(teams.values());
    }

    @Override
    public Location getSpawnLocation(GameTeam team) {
        List<Location> locations = spawns.get(team.getName());

        if (locations == null || locations.isEmpty()) return null;
        return locations.get(random.nextInt(locations.size()));
    }

    @Override
    public void addSpawnLocation(GameTeam team, Location location) {
        List<Location> locations = spawns.get(team.getName());
        if (locations == null) locations = Lists.newArrayList();

        locations.add(location);
        spawns.put(team.getName(), locations);
    }

    @Override
    public boolean hasSpawnLocation(GameTeam team) {
        List<Location> locations = spawns.get(team.getName());
        return locations != null && !locations.isEmpty();
    }

    @Override
    public Location getObjectiveLocation(GameTeam team) {
        return objectives.get(team.getName());
    }

    @Override
    public void setObjectiveLocation(GameTeam team, Location location) {
        objectives.put(team.getName(), location);
    }

    @Override
    public boolean hasObjectiveLocation(GameTeam team) {
        return objectives.containsKey(team.getName());
    }

    @Override
    public void save() {
        // TODO Auto-generated method stub

    }

}
