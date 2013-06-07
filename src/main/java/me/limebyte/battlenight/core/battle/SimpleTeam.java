package me.limebyte.battlenight.core.battle;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.core.tosort.Metadata;
import me.limebyte.battlenight.core.util.BattlePlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SimpleTeam implements Team {

    private String name;
    private String displayName;
    private ChatColor colour;
    private boolean ready = false;
    private HashSet<String> players = new HashSet<String>();

    public SimpleTeam(String name) {
        this(name, ChatColor.WHITE);
    }

    public SimpleTeam(String name, ChatColor colour) {
        this.name = name.toLowerCase();
        displayName = name;
        this.colour = colour;
    }

    public ChatColor getColour() {
        return colour;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

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

    @Override
    public int getScore() {
        int score = 0;
        Map<String, BattlePlayer> bPlayers = BattlePlayer.getPlayers();
        for (String name : players) {
            score += bPlayers.get(name).getStats().getScore();
        }
        return score;
    }

    @Override
    public Set<String> getPlayers() {
        return players;
    }

    @Override
    public void addPlayer(Player player) {
        players.add(player.getName());
        Metadata.set(player, "team", name);
    }

    @Override
    public void removePlayer(Player player) {
        players.add(player.getName());
        Metadata.remove(player, "team");
    }

}
