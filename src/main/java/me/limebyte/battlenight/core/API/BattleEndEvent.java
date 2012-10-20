package me.limebyte.battlenight.core.API;

import java.util.Map;

import me.limebyte.battlenight.core.battle.Team;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Thrown when the current Battle ends.
 */

public class BattleEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final String winningTeam;
    private final String losingTeam;
    private final Map<String, Team> winningPlayers;

    public BattleEndEvent(String winner, String loser, Map<String, Team> winners) {
        winningTeam = winner;
        losingTeam = loser;
        winningPlayers = winners;
    }

    public String getWinner() {
        return winningTeam;
    }

    public String getLosers() {
        return losingTeam;
    }

    public Player[] getWinningPlayers() throws NullPointerException {
        if (winningPlayers == null)
            throw new NullPointerException();

        final int numPlayers = winningPlayers.keySet().size();
        final Player[] output = new Player[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            output[i] = Bukkit.getServer().getPlayer(
                    (String) winningPlayers.keySet().toArray()[i]);
        }

        return output;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}