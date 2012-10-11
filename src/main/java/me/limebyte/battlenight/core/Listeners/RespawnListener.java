package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public RespawnListener(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final String name = player.getName();
        if (plugin.BattleUsersRespawn.containsKey(name)) {
            // If the Battle is still going on, take them to the spectator area
            // to watch
            if (plugin.battleInProgress) {
                event.setRespawnLocation(plugin.getCoords("spectator"));
                plugin.battle.resetPlayer(player, false, true);
                plugin.addSpectator(player, "death");
                // Else, take them to the exit area
            } else {
                event.setRespawnLocation(plugin.getCoords("exit"));
                plugin.battle.resetPlayer(player, false, false);
            }
            plugin.BattleUsersRespawn.remove(name);
        }
    }

}