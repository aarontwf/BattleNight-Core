package me.limebyte.battlenight.api.battle;

import java.util.List;

import org.bukkit.entity.Player;

public interface TeamedBattle extends Battle {

    public boolean addTeam(Team team);

    public boolean areEnemies(Player player1, Player player2);

    public List<Team> getLeadingTeams();

    public Team getTeam(Player player);

    public List<Team> getTeams();

    public boolean removeTeam(Team team);

    public void setTeam(Player player, Team team);

}
