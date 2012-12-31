package me.limebyte.battlenight.api.battle;

import java.util.Set;

public abstract class TeamedBattle extends Battle {

    Set<Team> teams;

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

}
