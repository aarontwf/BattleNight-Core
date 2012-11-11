package me.limebyte.battlenight.core.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.chat.Messaging;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    public static Set<String> toProcess = new HashSet<String>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (toProcess.contains(name)) {
            Messaging.debug(Level.INFO, "The respawn HashSet contained " + name + ".");

            if (BattleNight.getBattle().isInProgress()) {
                event.setRespawnLocation(Waypoint.SPECTATOR.getLocation());
                BattleNight.getBattle().resetPlayer(player, false);
                BattleNight.addSpectator(player, "death");
            } else {
                event.setRespawnLocation(Waypoint.EXIT.getLocation());
                BattleNight.getBattle().resetPlayer(player, false);
            }

            toProcess.remove(name);
        }
    }

}