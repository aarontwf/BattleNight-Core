package org.battlenight.core.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.battlenight.api.BattleAPI;
import org.battlenight.api.map.GameMap;
import org.battlenight.api.map.MapManager;

import com.google.common.collect.Maps;

public class SimpleMapManager implements MapManager {

    private BattleAPI api;
    private Map<String, GameMap> maps;

    public SimpleMapManager(BattleAPI api) {
        this.api = api;
        this.maps = Maps.newHashMap();

        loadMaps();
    }

    @Override
    public GameMap getMap(String name) {
        return maps.get(name.toLowerCase());
    }

    @Override
    public GameMap getRandomMap() {
        List<String> keys = new ArrayList<String>(maps.keySet());
        return keys.size() > 0 ? getMap(keys.get(api.getRandom().nextInt(keys.size()))) : null;
    }

    /**
     * Registers a {@link GameMap} with this manager.
     * 
     * @param map
     *            the map to register
     */
    public void register(GameMap map, String name) {
        maps.put(name.toLowerCase(), map);
    }

    private void loadMaps() {

    }

}
