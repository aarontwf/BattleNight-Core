package me.limebyte.battlenight.core.listeners;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.core.BattleNight;

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
            // If the Battle is still going on, take them to the spectator area
            // to watch
            if (BattleNight.getBattle().isInProgress()) {
                event.setRespawnLocation(BattleNight.getCoords("spectator"));
                BattleNight.getBattle().resetPlayer(player, false, true);
                BattleNight.addSpectator(player, "death");
                // Else, take them to the exit area
            } else {
                event.setRespawnLocation(BattleNight.getCoords("exit"));
                BattleNight.getBattle().resetPlayer(player, false, false);
            }
            toProcess.remove(name);
        }
    }

}