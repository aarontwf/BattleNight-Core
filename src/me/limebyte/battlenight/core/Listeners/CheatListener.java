package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class CheatListener implements Listener {

	// Get Main Class
	public static BattleNight plugin;
	public CheatListener(BattleNight instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
			Projectile projectile = event.getEntity();
			if(projectile.getShooter() instanceof Player) {
				Player thrower = (Player) projectile.getShooter();
				if (plugin.BattleUsersTeam.containsKey(thrower.getName()) && plugin.playersInLounge) {
					event.setCancelled(true);
					thrower.sendMessage(ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE + "Not so fast! No Cheating!");
				}
			}
	}
	
}
