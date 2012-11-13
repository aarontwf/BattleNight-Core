package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Battle;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (Metadata.getBoolean(player, "respawn")) {
            Battle battle = BattleNight.getBattle();

            if (battle.isInProgress() && !battle.isEnding()) {
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