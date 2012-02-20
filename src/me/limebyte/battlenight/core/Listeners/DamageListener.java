package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

	// Get Main Class
	public static BattleNight plugin;
	public DamageListener(BattleNight instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent)event;
			if(!canBeDamaged(subEvent)) {
				event.setCancelled(true);
			}
			else {
				event.setCancelled(false);
			}
		}
	}
	
	private boolean canBeDamaged(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity damaged = event.getEntity();
		
		if(!(damaged instanceof Player)) return true;
		
		if (damager == damaged) return true;
		if (damager instanceof Projectile) damager = ((Projectile)damager).getShooter();
		
		if(!(damager instanceof Player)) return true;
		
		if(!isInBattle((Player) damager) || !isInBattle((Player) damaged)) return true;
		
		if(areEnemies((Player) damager, (Player) damaged) && !plugin.playersInLounge) return true;
		
		return false;
	}
	
	private boolean isInBattle(Player player) {
		if (plugin.BattleUsersTeam.containsKey(player.getName())) return true;
		else return false;
	}
	
	private boolean areEnemies(Player player1, Player player2) {
		if(plugin.BattleUsersTeam.get(player1.getName()) == "red" && plugin.BattleUsersTeam.get(player2.getName()) == "blue") {
			return true;
		}
		else if(plugin.BattleUsersTeam.get(player1.getName()) == "blue" && plugin.BattleUsersTeam.get(player2.getName()) == "red") {
			return true;
		}
		else if(plugin.BattleUsersTeam.get(player2.getName()) == "red" && plugin.BattleUsersTeam.get(player1.getName()) == "blue") {
			return true;
		}
		else if(plugin.BattleUsersTeam.get(player2.getName()) == "blue" && plugin.BattleUsersTeam.get(player1.getName()) == "red") {
			return true;
		}
		else {
			return false;
		}
	}
	
}
