package me.limebyte.battlenight.core.api;

import java.util.Set;

import org.bukkit.entity.Player;

public interface Battle {

    public boolean start();

    public boolean end();

    public boolean isInProgress();

    public boolean addPlayer(Player player);

    public boolean removePlayer(Player player);

    public boolean containsPlayer(Player player);

    public Set<Player> getPlayers();

    public boolean addSpectator(Player player);

    public boolean removeSpectator(Player player);

    public boolean containsSpectator(Player player);

    public Set<Player> getSpectators();

    public Team getWinningTeam();

    public Set<Team> getTeams();

}
