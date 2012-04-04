package me.limebyte.battlenight.core.API;

import java.util.Map;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
* Thrown when the current Battle ends.
*/

public class BattleEndEvent extends Event {

	// Get Main Class
	public static BattleNight plugin;
	public BattleEndEvent(BattleNight instance) {
		plugin = instance;
	}
	
	private static final HandlerList handlers = new HandlerList();
	private String winningTeam;
	private String losingTeam;
	private Map<String, String> winningPlayers;
	
	public BattleEndEvent (String winner, String loser, Map<String, String> winners) {
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
		if(winningPlayers == null) throw new NullPointerException();
		
		int numPlayers = winningPlayers.keySet().size();
		Player[] output = new Player[numPlayers];
		
		for(int i=0; i<numPlayers; i++) {
			output[i] = Bukkit.getServer().getPlayer((String)winningPlayers.keySet().toArray()[i]);
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