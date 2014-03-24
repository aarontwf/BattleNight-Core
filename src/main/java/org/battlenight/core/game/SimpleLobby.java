package org.battlenight.core.game;

import java.util.List;
import java.util.Queue;

import org.battlenight.api.Util;
import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.game.Battle;
import org.battlenight.api.game.Lobby;
import org.battlenight.core.BattleNight;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class SimpleLobby implements Lobby {

    private BattleNight plugin;
    private List<Battle> battles;
    // TODO LobbyTimer timer;
    private Queue<String> players;

    public SimpleLobby(BattleNight plugin) {
        this.plugin = plugin;
        this.battles = Lists.newArrayList();
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
        Util.reset(player);
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

    @Override
    public List<Battle> getBattles() {
        return battles;
    }

}
