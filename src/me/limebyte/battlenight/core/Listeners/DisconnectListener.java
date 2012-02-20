package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {
	
	// Get Main Class
	public static BattleNight plugin;
	public DisconnectListener(BattleNight instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (plugin.BattleUsersTeam.containsKey(player.getName())) {
			plugin.removePlayer(player, "has been removed from the Battle as they disconnected from the server.", null);
		}
		else if (plugin.BattleSpectators.containsKey(player.getName())) {
			plugin.removeSpectator(player);
		}
		/*if (plugin.BattleUsersTeam.containsKey(player.getName())) {
			if (plugin.BattleUsersTeam.get(player.getName()) == "red") {
				plugin.redTeam -= 1;
			}
			else {
				plugin.blueTeam -= 1;
			}
			im.clearInv(player);
			player.getInventory().clear();
			plugin.BattleUsersTeam.remove(player.getName());
			plugin.BattleUsersClass.remove(player.getName());
			plugin.goToWaypoint(player, "exit");
		}*/
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if (plugin.BattleUsersTeam.containsKey(player.getName())) {
			plugin.removePlayer(player, "has been removed from the Battle as they were kicked from the server.", null);
		}
		else if (plugin.BattleSpectators.containsKey(player.getName())) {
			plugin.removeSpectator(player);
		}
	}
}