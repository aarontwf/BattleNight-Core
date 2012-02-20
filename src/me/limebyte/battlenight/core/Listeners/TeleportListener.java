package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {
  
	// Get Main Class
	public static BattleNight plugin;
	public TeleportListener(BattleNight instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();

		if ((plugin.BattleUsersTeam.containsKey(player.getName())) && 
				(!plugin.BattleTelePass.containsKey(player.getName()))) {
			event.setCancelled(true);
			plugin.tellPlayer(player, Track.NO_TP);
		}
	}
}