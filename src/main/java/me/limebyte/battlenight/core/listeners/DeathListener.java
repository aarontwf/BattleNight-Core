package me.limebyte.battlenight.core.listeners;

import java.util.HashMap;
import java.util.Map;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleDeathEvent;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.OldBattle;
import me.limebyte.battlenight.core.old.Waypoint;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener {

    private Map<String, BattleDeathEvent> queue = new HashMap<String, BattleDeathEvent>();

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
            event.getDrops().clear();
            event.setDeathMessage("");

            BattleDeathEvent apiEvent = new BattleDeathEvent(battle, player);
            Bukkit.getServer().getPluginManager().callEvent(apiEvent);

            if (apiEvent.isCancelled()) {
                apiEvent.setRespawnLocation(battle.toSpectator(player, true));
            }

            queue.put(player.getName(), apiEvent);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

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

        if (queue.containsKey(name)) {
            BattleDeathEvent apiEvent = queue.get(name);
            queue.remove(name);

            Battle battle = BattleNight.instance.getAPI().getBattle();

            if (apiEvent.isCancelled()) {
                battle.respawn(player);
            } else {
                PlayerData.reset(player);
                PlayerData.restore(player, false, false);
            }

            event.setRespawnLocation(apiEvent.getRespawnLocation());
        }
    }

}