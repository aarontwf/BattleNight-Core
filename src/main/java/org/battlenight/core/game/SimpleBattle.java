package org.battlenight.core.game;

import java.util.List;

import org.battlenight.api.game.Battle;
import org.battlenight.api.game.type.GameType;
import org.bukkit.entity.Player;

public class SimpleBattle implements Battle {

    private GameType gameType;

    public SimpleBattle(GameType gameType) {
        this.gameType = gameType;
    }

    public void addPlayer(Player player) {

    }

    public void removePlayer(Player player) {

    }

    @Override
    public List<String> getPlayers() {
        return null;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

}