package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleRespawnEvent;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.OldBattle;
import me.limebyte.battlenight.core.old.Waypoint;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        String name = player.getName();

        if (BattleNight.getBattle().usersTeam.containsKey(name)) {
            event.getDrops().clear();
            event.setDeathMessage("");

            Metadata.set(player, "respawn", true);

            if (!BattleNight.getBattle().isInLounge()) {
                Messenger.killFeed(player, player.getKiller());
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleNight.instance, new Runnable() {
                @Override
                public void run() {
                    BattleNight.getBattle().removePlayer(player, true, null, null);
                }
            }, 1L);
        }

        Battle battle = BattleNight.instance.getAPI().getBattle();
        if (battle.containsPlayer(player)) {
            Metadata.set(player, "HandleRespawn", true);
            event.getDrops().clear();
            event.setDeathMessage("");
        }
    }

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

            Battle battle = BattleNight.instance.getAPI().getBattle();
            BattleRespawnEvent apiEvent = new BattleRespawnEvent(battle, player);
            Bukkit.getServer().getPluginManager().callEvent(apiEvent);

            Location loc;
            if (apiEvent.isCancelled()) {
                loc = battle.toSpectator(player);
            } else {
                loc = battle.respawn(player);
            }
            if (loc != null) event.setRespawnLocation(loc);
        }
    }

}