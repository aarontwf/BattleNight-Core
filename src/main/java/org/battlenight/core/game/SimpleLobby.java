package org.battlenight.core.game;

import java.util.List;
import java.util.Queue;

import org.battlenight.api.Util;
import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.game.Battle;
import org.battlenight.api.game.Lobby;
import org.battlenight.core.BattleNight;
import org.battlenight.core.timer.LobbyTimer;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class SimpleLobby implements Lobby {

    private BattleNight plugin;
    private List<Battle> battles;
    private LobbyTimer timer;
    private Queue<String> players;

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
        players.add(player.getName());
        plugin.getMessenger().send(player, "lobby.join");
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player.getName());
        player.loadData();
        plugin.getMessenger().send(player, "lobby.leave");
    }

    public void startBattle() {

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
