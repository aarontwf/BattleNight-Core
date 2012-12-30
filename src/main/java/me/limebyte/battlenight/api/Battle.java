package me.limebyte.battlenight.api;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.api.util.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public abstract class Battle {

    private Arena arena;
    private boolean inProgress = false;

    private Set<String> players = new HashSet<String>();
    private Set<String> spectators = new HashSet<String>();

    public boolean start() {
        inProgress = true;
        onStart();
        return true;
    }

    public boolean end() {
        inProgress = false;
        onEnd();
        return true;
    }

    public abstract void onStart();

    public abstract void onEnd();

    public boolean isInProgress() {
        return inProgress;
    }

    public boolean addPlayer(Player player) {
        PlayerData.store(player);
        PlayerData.reset(player);
        players.add(player.getName());
        return true;
    }

    public boolean removePlayer(Player player) {
        PlayerData.restore(player, true, false);
        players.remove(player.getName());
        return true;
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player.getName());
    }

    public Set<String> getPlayers() {
        return players;
    }

    public boolean addSpectator(Player player) {
        spectators.add(player.getName());
        return true;
    }

    public boolean removeSpectator(Player player) {
        spectators.remove(player.getName());
        return true;
    }

    public boolean containsSpectator(Player player) {
        return spectators.contains(player.getName());
    }

    public Set<String> getSpectators() {
        return spectators;
    }

    public Player getLeadingPlayer() {
        return null;
    }

    public Arena getArena() {
        return arena;
    }

    public boolean setArena(Arena arena) {
        if (isInProgress()) return false;
        this.arena = arena;
        return true;
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        // TODO Auto-generated method stub

    }

    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // TODO Auto-generated method stub

    }

}
