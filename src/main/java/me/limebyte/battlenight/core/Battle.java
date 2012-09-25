package me.limebyte.battlenight.core;

import me.limebyte.battlenight.core.API.BattleEndEvent;
import me.limebyte.battlenight.core.BattleNight.WPoint;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Battle {

	BattleNight plugin;
	
	public Battle(BattleNight plugin) {
		this.plugin = plugin;
	}
	
	public void addPlayer(Player player) {
		if (plugin.preparePlayer(player)) {
			String name = player.getName();
			
			if (plugin.blueTeam > plugin.redTeam) {
				plugin.goToWaypoint(player, WPoint.RED_LOUNGE);
				plugin.BattleUsersTeam.put(name, "red");
				plugin.tellPlayer(player, "Welcome! You are on team " + ChatColor.RED + "<Red>");
				plugin.tellEveryoneExcept(player, name + " has joined team " + ChatColor.RED + "<Red>");
				plugin.redTeam ++;
			} else {
				plugin.goToWaypoint(player, WPoint.BLUE_LOUNGE);
				plugin.BattleUsersTeam.put(name, "blue");
				plugin.tellPlayer(player, "Welcome! You are on team " + ChatColor.BLUE + "<Blue>");
				plugin.tellEveryoneExcept(player, name + " has joined team " + ChatColor.BLUE + "<Blue>");
				plugin.blueTeam ++;
			}
			
			plugin.playersInLounge = true;
		} else {
			plugin.tellPlayer(player, Track.MUST_HAVE_EMPTY);
		}
	}

	public void removePlayer(Player player, boolean death, String msg1, String msg2) {
		String name = player.getName();
		
		if (plugin.BattleUsersTeam.containsKey(name)) {
			String team = plugin.BattleUsersTeam.get(name);
			boolean sendMsg1 = msg1 != null;
			
			if (team == "red") {
				plugin.redTeam--;
				if (sendMsg1) plugin.tellEveryoneExcept(player, ChatColor.RED + name	+ ChatColor.WHITE + " " + msg1);
			}
			if (team == "blue") {
				plugin.blueTeam--;
				if (sendMsg1) plugin.tellEveryoneExcept(player,	ChatColor.BLUE + name + ChatColor.WHITE	+ " " + msg1);
			}
			
			if (msg2 != null) {
				plugin.tellPlayer(player, msg2);
			}
			
			// If red or blue won
			if (plugin.redTeam == 0 || plugin.blueTeam == 0) {
				
				// If the battle started
				if (!plugin.playersInLounge) {
					// If red won
					if (plugin.redTeam > 0) {
						plugin.tellEveryone(Track.RED_WON);
						Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("red", "blue", plugin.BattleUsersTeam));
					// If blue won
					} else if (plugin.blueTeam > 0) {
						plugin.tellEveryone(Track.BLUE_WON);
						Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("blue", "red", plugin.BattleUsersTeam));
					// If neither team won
					} else {
						plugin.tellEveryone(Track.DRAW);
						Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("draw", "draw", null));
					}
				}
				
				for (String currentName : plugin.BattleUsersTeam.keySet()) {
					if (Bukkit.getPlayer(currentName) != null) {
						Player currentPlayer = Bukkit.getPlayer(currentName);
						if (!(death && currentPlayer == player)) {
							resetPlayer(currentPlayer, true, false);
						}
					}
				}
				
				resetBattle();
			}
			
			if (!death) resetPlayer(player, true, true);
		} else {
			BattleNight.log.info("[BattleNight] Failed to remove player '"+name+"' from the Battle as they are not in it.");
		}
	}
	
	public void resetPlayer(Player player, boolean teleport, boolean removeHash) {
		player.getInventory().clear();
		plugin.clearArmorSlots(player);
		plugin.removePotionEffects(player);
		plugin.restorePlayer(player);
		if (teleport) plugin.goToWaypoint(player, WPoint.EXIT);
		plugin.cleanSigns(player);
		
		if (removeHash) {
			plugin.BattleUsersTeam.remove(player.getName());
			plugin.BattleUsersClass.remove(player.getName());
		}
	}
	
	private void resetBattle() {
		plugin.removeAllSpectators();
		plugin.cleanSigns();
		plugin.BattleSigns.clear();
		plugin.battleInProgress = false;
		plugin.redTeamIronClicked = false;
		plugin.blueTeamIronClicked = false;
		plugin.BattleUsersTeam.clear();
		plugin.BattleUsersClass.clear();
		plugin.redTeam = 0;
		plugin.blueTeam = 0;
	}
	
	public void end() {
		if (plugin.blueTeam > plugin.redTeam) {
			plugin.tellEveryone(Track.BLUE_WON);
			Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("blue", "red", plugin.BattleUsersTeam));
		} else if (plugin.redTeam > plugin.blueTeam) {
			plugin.tellEveryone(Track.RED_WON);
			Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("red", "blue", plugin.BattleUsersTeam));
		} else {
			plugin.tellEveryone(Track.DRAW);
			Bukkit.getServer().getPluginManager().callEvent(new BattleEndEvent("draw", "draw", null));
		}
		
		for (String currentName : plugin.BattleUsersTeam.keySet()) {
			if (Bukkit.getPlayer(currentName) != null) {
				Player currentPlayer = Bukkit.getPlayer(currentName);
				resetPlayer(currentPlayer, true, false);
			}
		}
		
		resetBattle();
		
		plugin.removeAllSpectators();
	}
}
