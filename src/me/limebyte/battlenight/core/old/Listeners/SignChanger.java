package me.limebyte.battlenight.core.old.Listeners;

import me.limebyte.battlenight.core.old.BattleNight;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChanger implements Listener {
	
	// Get Main Class
	public static BattleNight plugin;
	public SignChanger(BattleNight instance) {
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent e) {
		Player player = e.getPlayer();
		if ((plugin.ClassList.contains(e.getLine(0))) && (!e.getLine(1).isEmpty()) && (!e.getLine(2).isEmpty()) && (!e.getLine(3).isEmpty())) {
			player.sendMessage(BattleNight.BNTag + "Error creating sign for " + e.getLine(0) + "!  Leave last 3 lines blank.");
		}
		else if ((plugin.ClassList.contains(e.getLine(0))) && (e.getLine(1).isEmpty()) && (e.getLine(2).isEmpty()) && (e.getLine(3).isEmpty())) {
			e.setLine(1, "--------");
			player.sendMessage(BattleNight.BNTag + "Successfully created sign for " + e.getLine(0) + "!");
		}
	}
}