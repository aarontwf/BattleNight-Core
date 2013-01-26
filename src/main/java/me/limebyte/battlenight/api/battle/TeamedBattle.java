package me.limebyte.battlenight.api.battle;

import java.util.Set;

import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.entity.Player;

public abstract class TeamedBattle extends Battle {

    private Set<Team> teams;

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

    public Team getLeadingTeam() {
        return null;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    @Override
    public abstract void onStart();

    @Override
    public abstract void onEnd();

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

}
