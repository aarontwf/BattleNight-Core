package org.battlenight.core.game;

import java.util.Map;

import org.battlenight.api.BattleAPI;
import org.battlenight.api.game.type.GameType;
import org.battlenight.api.game.type.GameTypeManager;
import org.battlenight.core.game.type.CaptureTheFlag;

import com.google.common.collect.Maps;

public class SimpleGameTypeManager implements GameTypeManager {

    private BattleAPI api;
    private Map<String, Class<? extends GameType>> gameTypes;

    public SimpleGameTypeManager(BattleAPI api) {
        this.api = api;
        this.gameTypes = Maps.newHashMap();

        register(CaptureTheFlag.class, "capture-the-flag");
    }

    @Override
    public GameType getGameType(String name) {
        if (!gameTypes.containsKey(name.toLowerCase())) return null;

        GameType type = null;

        try {
            type = gameTypes.get(name.toLowerCase()).newInstance();
        } catch (Exception e) {
            api.getMessenger().logError("gametype.registration-failed", gameTypes.get(name.toLowerCase()));
        }

        return type;
    }

    /**
     * Registers a {@link GameType} with this manager.
     * 
     * @param gameType
     *            the game type to register
     */
    public void register(Class<? extends GameType> clazz, String name) {
        gameTypes.put(name.toLowerCase(), clazz);
    }

}
