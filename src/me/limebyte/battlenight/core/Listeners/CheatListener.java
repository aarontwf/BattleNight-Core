package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheatListener implements Listener {

	// Get Main Class
	public static BattleNight plugin;

	public CheatListener(BattleNight instance) {
		plugin = instance;
	}

	// //////////////////
	// General Events //
	// //////////////////

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		if (plugin.BattleUsersTeam.containsKey(player.getName())
				&& !plugin.BattleTelePass.containsKey(player.getName())) {
			switch (event.getCause()) {
			case COMMAND:
			case PLUGIN:
				event.setCancelled(true);
				plugin.tellPlayer(player, Track.NO_TP);
				break;
			case ENDER_PEARL:
				;
				if (!plugin.config
						.getBoolean("Teleportation.EnderPearls", true)) {
					event.setCancelled(true);
					plugin.tellPlayer(player, Track.NO_TP);
				}
				break;
			case NETHER_PORTAL:
			case END_PORTAL:
				if (!plugin.config.getBoolean("Teleportation.Portals", false)) {
					event.setCancelled(true);
					plugin.tellPlayer(player, Track.NO_TP);
				}
				break;
			default:
				event.setCancelled(true);
				break;
			}
		}
	}

	// //////////////////
	// Lounge Events //
	// //////////////////

	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile projectile = event.getEntity();
		if (projectile.getShooter() instanceof Player) {
			Player thrower = (Player) projectile.getShooter();
			if (plugin.BattleUsersTeam.containsKey(thrower.getName())
					&& plugin.playersInLounge) {
				event.setCancelled(true);
				plugin.tellPlayer(thrower, Track.NO_CHEATING);
			}
		}
	}

}
