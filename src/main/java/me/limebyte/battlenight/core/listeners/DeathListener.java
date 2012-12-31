package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Metadata;

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
            battle.onPlayerDeath(event);
        }
    }

}