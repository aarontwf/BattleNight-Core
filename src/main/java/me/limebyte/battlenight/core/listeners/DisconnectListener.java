package me.limebyte.battlenight.core.listeners;

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
        final Player player = event.getPlayer();
        if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
            BattleNight.battle.removePlayer(player, false, "has been removed from the Battle as they disconnected from the server.", null);
        } else if (BattleNight.getBattle().spectators.contains(player.getName())) {
            BattleNight.removeSpectator(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        final Player player = event.getPlayer();
        if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
            BattleNight.battle.removePlayer(player, false, "has been removed from the Battle as they were kicked from the server.", null);
        } else if (BattleNight.getBattle().spectators.contains(player.getName())) {
            BattleNight.removeSpectator(player);
        }
    }
}