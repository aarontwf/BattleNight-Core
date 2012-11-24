package me.limebyte.battlenight.api;

import java.util.Set;

public interface TeamedBattle extends Battle {

    public Team getLeadingTeam();

    public Set<Team> getTeams();

}
