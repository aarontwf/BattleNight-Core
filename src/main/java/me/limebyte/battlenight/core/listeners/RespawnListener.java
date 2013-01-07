package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleRespawnEvent;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.OldBattle;
import me.limebyte.battlenight.core.old.Waypoint;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
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
            OldBattle battle = BattleNight.getBattle();

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
            Metadata.set(player, "HandleRespawn", false);
            BattleRespawnEvent apiEvent = new BattleRespawnEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(apiEvent);

            Battle battle = BattleNight.instance.getAPI().getBattle();
            if (apiEvent.isCancelled()) {
                battle.toSpectator(player);
            } else {
                battle.respawn(player);
                event.setRespawnLocation(battle.getArena().getRandomSpawnPoint().getLocation());
            }
        }
    }
}