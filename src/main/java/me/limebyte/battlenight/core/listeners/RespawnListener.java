package me.limebyte.battlenight.core.listeners;

import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.chat.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (Metadata.getBoolean(player, "respawn")) {
            Messaging.debug(Level.INFO, player.getName() + "'s respawn event has been handled.");

            if (BattleNight.getBattle().isInProgress()) {
                event.setRespawnLocation(Waypoint.SPECTATOR.getLocation());
                BattleNight.getBattle().resetPlayer(player, false, null);
                BattleNight.addSpectator(player, "death");
            } else {
                event.setRespawnLocation(Waypoint.EXIT.getLocation());
                BattleNight.getBattle().resetPlayer(player, false, null);
            }

            Metadata.set(player, "respawn", false);
        }
    }

}