package org.battlenight.core.game;

import java.util.List;
import java.util.UUID;

import org.battlenight.api.game.Battle;
import org.battlenight.api.game.type.GameType;
import org.battlenight.api.map.GameMap;
import org.bukkit.entity.Player;

public class SimpleBattle implements Battle {

    private GameType gameType;
    private GameMap gameMap;
    private boolean inProgress;
    private List<UUID> players;

    public SimpleBattle(GameType gameType, GameMap gameMap) {
        this.gameType = gameType;
        this.gameMap = gameMap;
        this.inProgress = false;
    }

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    @Override
    public List<UUID> getPlayers() {
        return players;
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public GameMap getGameMap() {
        return gameMap;
    }

    @Override
    public boolean isInProgress() {
        return inProgress;
    }

}
