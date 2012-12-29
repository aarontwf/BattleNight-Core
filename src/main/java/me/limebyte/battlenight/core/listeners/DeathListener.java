package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.chat.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    // Called when player dies
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        String name = player.getName();

        if (BattleNight.getBattle().usersTeam.containsKey(name)) {
            event.getDrops().clear();
            event.setDeathMessage("");

            Metadata.set(player, "respawn", true);

            if (!BattleNight.getBattle().isInLounge()) {
                Messaging.killFeed(player, player.getKiller());
            }

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleNight.getInstance(), new Runnable() {
                @Override
                public void run() {
                    BattleNight.getBattle().removePlayer(player, true, null, null);
                }
            }, 1L);
        }

        if (BattleNight.getInstance().getAPI().getBattle().containsPlayer(player)) {
            Metadata.set(player, "HandleRespawn", true);
            BattleNight.getInstance().getAPI().getBattle().onPlayerDeath(event);
        }
    }

}