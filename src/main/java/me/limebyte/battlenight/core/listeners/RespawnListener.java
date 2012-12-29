package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.PlayerData;
import me.limebyte.battlenight.core.battle.Battle;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

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
                // Player becomes a Spectator
                event.setRespawnLocation(Waypoint.SPECTATOR.getLocation());
                battle.resetPlayer(player, false, null, true);
                battle.addSpectator(player, "death");
            } else {
                // Last death
                if (PlayerData.getSavedLocation(player) != null && !ConfigManager.get(Config.MAIN).getBoolean("ExitWaypoint", false)) {
                    event.setRespawnLocation(PlayerData.getSavedLocation(player));
                } else {
                    event.setRespawnLocation(Waypoint.EXIT.getLocation());
                }
                battle.resetPlayer(player, false, null, false);
            }

            Metadata.set(player, "respawn", false);
        }

        if (Metadata.getBoolean(player, "HandleRespawn")) {
            BattleNight.getInstance().getAPI().getBattle().onPlayerRespawn(event);
            Metadata.set(player, "HandleRespawn", false);
        }
    }

}