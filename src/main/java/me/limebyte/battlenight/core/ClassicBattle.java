package me.limebyte.battlenight.core;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.api.Arena;
import me.limebyte.battlenight.api.Team;
import me.limebyte.battlenight.api.TeamedBattle;
import me.limebyte.battlenight.core.battle.Waypoint;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClassicBattle implements TeamedBattle {

    private BattleNight plugin;
    private Arena arena;
    private Team teamA = new SimpleTeam("Red", ChatColor.RED);
    private Team teamB = new SimpleTeam("Blue", ChatColor.BLUE);
    private boolean inProgress = false;

    private Set<String> players;
    private Set<String> spectators;

    public ClassicBattle(BattleNight plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean start() {
        inProgress = true;
        return true;
    }

    @Override
    public boolean end() {
        inProgress = false;
        teamA.setKills(0);
        teamB.setKills(0);
        return true;
    }

    @Override
    public boolean isInProgress() {
        return inProgress;
    }

    @Override
    public boolean addPlayer(Player player) {
        players.add(player.getName());
        plugin.getAPI().getUtil().preparePlayer(player, Waypoint.BLUE_LOUNGE.getLocation());
        return true;
    }

    @Override
    public boolean removePlayer(Player player) {
        players.remove(player.getName());
        plugin.getAPI().getUtil().restorePlayer(player);
        return true;
    }

    @Override
    public boolean containsPlayer(Player player) {
        return players.contains(player.getName());
    }

    @Override
    public Set<String> getPlayers() {
        return players;
    }

    @Override
    public boolean addSpectator(Player player) {
        spectators.add(player.getName());
        return true;
    }

    @Override
    public boolean removeSpectator(Player player) {
        spectators.remove(player.getName());
        return true;
    }

    @Override
    public boolean containsSpectator(Player player) {
        return spectators.contains(player.getName());
    }

    @Override
    public Set<String> getSpectators() {
        return spectators;
    }

    @Override
    public Team getLeadingTeam() {
        if (teamA.getKills() == teamB.getKills()) return null;
        return teamA.getKills() > teamB.getKills() ? teamA : teamB;
    }

    @Override
    public Set<Team> getTeams() {
        Set<Team> teams = new HashSet<Team>();
        teams.add(teamA);
        teams.add(teamB);
        return teams;
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public boolean setArena(Arena arena) {
        if (isInProgress()) return false;
        this.arena = arena;
        return true;
    }

    @Override
    public Player getLeadingPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

}
