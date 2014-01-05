package org.battlenight.core.game;

import java.util.Queue;

import org.battlenight.api.Util;
import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.game.Lobby;
import org.battlenight.core.BattleNight;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class SimpleLobby implements Lobby {

    private BattleNight plugin;
    // TODO List<Battle> battles;
    // TODO LobbyTimer timer;
    private Queue<String> players;

    public SimpleLobby(BattleNight plugin) {
        this.plugin = plugin;
        this.players = Lists.newLinkedList();
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
        players.add(player.getName());
        plugin.getMessenger().send(player, "lobby.join");
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player.getName());
        player.loadData();
        plugin.getMessenger().send(player, "lobby.leave");
    }

    @Override
    public Queue<String> getPlayers() {
        return players;
    }

}
