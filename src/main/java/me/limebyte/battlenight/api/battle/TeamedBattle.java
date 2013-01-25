package me.limebyte.battlenight.api.battle;

import java.util.Set;

public abstract class TeamedBattle extends Battle {

    private Set<Team> teams;

    public boolean addTeam(Team team) {
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
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

}
