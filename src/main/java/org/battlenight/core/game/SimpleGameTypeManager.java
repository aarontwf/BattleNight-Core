package org.battlenight.core.game;

import java.util.Map;

import org.battlenight.api.game.GameType;
import org.battlenight.api.game.GameTypeManager;

import com.google.common.collect.Maps;

public class SimpleGameTypeManager implements GameTypeManager {

    private Map<String, GameType> gameTypes;

    public SimpleGameTypeManager() {
        this.gameTypes = Maps.newHashMap();
    }

    @Override
    public GameType getGameType(String name) {
        return gameTypes.get(name.toLowerCase());
    }

    /**
     * Registers a {@link GameType} with this manager.
     * 
     * @param gameType
     *            the game type to register
     */
    public void register(GameType gameType) {
        gameTypes.put(gameType.getName().toLowerCase(), gameType);
    }

}
