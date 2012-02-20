package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropListener implements Listener {

	// Get Main Class
	public static BattleNight plugin;
	public DropListener(BattleNight instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (plugin.BattleUsersTeam.containsKey(player.getName())) {
			player.sendMessage(ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE + "Not so fast! No Cheating!");
			event.setCancelled(true);
		}
	}
}