package me.limebyte.battlenight.core.battle;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.core.util.player.BattlePlayer;
import me.limebyte.battlenight.core.util.player.Metadata;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SimpleTeam implements Team {

    private String name;
    private String displayName;
    private ChatColor colour;
    private boolean ready = false;
    private HashSet<UUID> players = new HashSet<UUID>();

    public SimpleTeam(String name) {
        this(name, ChatColor.WHITE);
    }

    public SimpleTeam(String name, ChatColor colour) {
        this.name = name.toLowerCase();
        displayName = name;
        this.colour = colour;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
        Metadata.set(player, "team", name);
    }

    @Override
    public ChatColor getColour() {
        return colour;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<UUID> getPlayers() {
        return players;
    }

    @Override
    public int getScore() {
        int score = 0;
        Map<UUID, BattlePlayer> bPlayers = BattlePlayer.getPlayers();
        for (UUID id : players) {
            score += bPlayers.get(id).getStats().getScore();
        }
        return score;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void removePlayer(Player player) {
        players.add(player.getUniqueId());
        Metadata.remove(player, "team");
    }

    @Override
    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return colour + name;
    }

    protected void reset(SimpleBattle battle) {
        ready = false;
        players.clear();
    }

}
