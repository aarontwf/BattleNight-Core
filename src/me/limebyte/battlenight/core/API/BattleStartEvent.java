package me.limebyte.battlenight.core.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
* Thrown when both teams are ready.
*/

public class BattleStartEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private Player[] battlenightPlayers;
	
	public BattleStartEvent (Player[] players) {
		battlenightPlayers = players;
	}
	
	public Player[] getPlayers() {
		return battlenightPlayers;
	}
	
    @Override
	public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }
	
}
