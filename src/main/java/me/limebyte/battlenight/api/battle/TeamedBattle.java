package me.limebyte.battlenight.api.battle;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class TeamedBattle extends Battle {

    private List<Team> teams = new ArrayList<Team>();

    public boolean addTeam(Team team) {
        String name = team.getName();
        for (Team t : teams) {
            if (t.getName().equals(name)) return false;
        }
        return teams.add(team);
    }

    public boolean removeTeam(Team team) {
        return teams.remove(team);
    }

    public List<Team> getLeadingTeams() {
        if (teams.size() == 0) return null;

        List<Team> leading = new ArrayList<Team>();
        for (Team team : teams) {
            if (leading.isEmpty()) {
                leading.add(team);
                continue;
            }

            int leadingKills = leading.get(0).getKills();

            if (leadingKills == team.getKills()) {
                leading.add(team);
                continue;
            }

            if (leadingKills < team.getKills()) {
                leading.clear();
                leading.add(team);
                continue;
            }
        }

        return leading;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public boolean addPlayer(Player player) {
        if (!assignTeam(player)) return false;
        return super.addPlayer(player);
    }

    @Override
    public boolean removePlayer(Player player) {
        setTeam(player, null);
        return super.removePlayer(player);
    }

    private boolean assignTeam(Player player) {
        if (teams.size() < 2) return false;

        Team smallest = null;
        for (Team team : teams) {
            if (smallest == null || team.getSize() < smallest.getSize()) smallest = team;
        }

        setTeam(player, smallest);
        return true;
    }

    public void setTeam(Player player, Team team) {
        String prevTeam = Metadata.getString(player, "team");
        if (prevTeam != null) {
            for (Team t : teams) {
                if (t.getName().equals(prevTeam)) {
                    t.decrementSize();
                    break;
                }
            }
        }

        if (team != null) {
            Metadata.set(player, "team", team.getName());
            team.incrementSize();
        } else {
            Metadata.remove(player, "team");
        }
    }

    public Team getTeam(Player player) {
        String teamName = Metadata.getString(player, "team");
        if (teamName != null) {
            for (Team team : teams) {
                if (team.getName().equals(teamName)) return team;
            }
        }
        return null;
    }

    public boolean areEnemies(Player player1, Player player2) {
        return (!getTeam(player1).getName().equals(getTeam(player2).getName()));
    }

    @Override
    public Location toSpectator(Player player, boolean death) {
        // TODO Auto-generated method stub
        return null;
    }
}
