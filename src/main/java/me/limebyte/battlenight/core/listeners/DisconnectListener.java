package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.Battle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        removePlayer(player, "disconnected");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        removePlayer(player, "kicked");
    }

    private void removePlayer(Player player, String reason) {
        Battle battle = BattleNight.getBattle();
        String name = player.getName();

        if (battle.usersTeam.containsKey(name)) {
            battle.removePlayer(player, false, "has been removed from the Battle as they " + reason + " from the server.", null);
        }

        if (battle.spectators.contains(name)) {
            battle.removeSpectator(player, null);
        }
    }
}