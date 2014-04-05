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
    }

    @Override
    public void addPlayer(Player player) {
        if (players.contains(player.getUniqueId())) return;

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

        updateTimer();
    }

    @Override
    public void removePlayer(Player player) {
        if (!players.contains(player.getUniqueId())) return;
        players.remove(player.getUniqueId());
        player.loadData();
        plugin.getMessenger().send(player, "lobby.leave");

        updateTimer();
    }

    public void startBattle() {
        if (players.isEmpty()) return;

        Battle battle = getNextBattle();
        if (battle == null) {
            updateTimer();
            return;
        }

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

    private void updateTimer() {
        if (players.size() > 0) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    private Battle getNextBattle() {
        GameType type = plugin.getGameTypeManager().getRandomGameType();
        GameMap map = plugin.getMapManager().getRandomMap();

        if (map == null) {
            plugin.getMessenger().sendLobby("lobby.no-maps");
            return null;
        }

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
