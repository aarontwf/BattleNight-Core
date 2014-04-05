package org.battlenight.core.game;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.battlenight.api.Util;
import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.game.Battle;
import org.battlenight.api.game.Lobby;
import org.battlenight.api.game.type.GameType;
import org.battlenight.api.map.GameMap;
import org.battlenight.core.BattleNight;
import org.battlenight.core.timer.LobbyTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class SimpleLobby implements Lobby {

    private BattleNight plugin;
    private List<Battle> battles;
    private LobbyTimer timer;
    private Queue<UUID> players;

    public SimpleLobby(BattleNight plugin) {
        this.plugin = plugin;
        this.battles = Lists.newArrayList();
        this.timer = new LobbyTimer(plugin, this, 120);
        this.players = Lists.newLinkedList();

        timer.start();
    }

    @Override
    public void addPlayer(Player player) {
        String spawn = plugin.getConfiguration().get(ConfigFile.LOCATIONS).getString("lobby.join");

        if (spawn == null) {
            plugin.getMessenger().send(player, "lobby.not-setup");
            return;
        }

        player.saveData();
        player.teleport(Util.locationFromString(spawn));
        Util.reset(player);
        players.add(player.getUniqueId());
        plugin.getMessenger().send(player, "lobby.join");

        if (players.size() < 2) timer.start();
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        player.loadData();
        plugin.getMessenger().send(player, "lobby.leave");

        if (players.size() < 1) timer.stop();
    }

    public void startBattle() {
        if (players.isEmpty()) return;

        Battle battle = getNextBattle();
        int max = battle.getGameType().getMaxPlayers();

        while (battle.getPlayers().size() < max && !players.isEmpty()) {
            battle.addPlayer(Bukkit.getPlayer(players.poll()));
        }
    }

    @Override
    public Queue<UUID> getPlayers() {
        return players;
    }

    @Override
    public List<Battle> getBattles() {
        return battles;
    }

    private Battle getNextBattle() {
        GameType type = plugin.getGameTypeManager().getRandomGameType();
        GameMap map = plugin.getMapManager().getRandomMap();

        Iterator<Battle> it = battles.iterator();
        while (it.hasNext()) {
            Battle b = it.next();
            if (!b.isInProgress()) it.remove();
        }

        Battle battle = new SimpleBattle(type, map);
        battles.add(battle);
        return battle;
    }
}
