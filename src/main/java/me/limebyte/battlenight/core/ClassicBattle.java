package me.limebyte.battlenight.core;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.api.Arena;
import me.limebyte.battlenight.api.Battle;
import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.Team;
import me.limebyte.battlenight.core.battle.Waypoint;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClassicBattle implements Battle {

    private Arena arena;
    private Team teamA = new SimpleTeam("Red", ChatColor.RED);
    private Team teamB = new SimpleTeam("Blue", ChatColor.BLUE);
    private boolean inProgress = false;

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
        BattleNightAPI.getUtil().preparePlayer(player, Waypoint.BLUE_LOUNGE.getLocation());
        return true;
    }

    @Override
    public boolean removePlayer(Player player) {
        BattleNightAPI.getUtil().restorePlayer(player);
        return true;
    }

    @Override
    public boolean containsPlayer(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<Player> getPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean addSpectator(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeSpectator(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsSpectator(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<Player> getSpectators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Team getWinningTeam() {
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

}
