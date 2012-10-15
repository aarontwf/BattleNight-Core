package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheatListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public CheatListener(BattleNight instance) {
        plugin = instance;
    }

    // //////////////////
    // General Events //
    // //////////////////

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        if (BattleNight.BattleUsersTeam.containsKey(player.getName()) && !BattleNight.BattleTelePass.containsKey(player.getName())) {
            switch (event.getCause()) {
                case COMMAND:
                case PLUGIN:
                    event.setCancelled(true);
                    BattleNight.tellPlayer(player, Track.NO_TP);
                    break;
                case ENDER_PEARL:
                    ;
                    if (!BattleNight.config
                            .getBoolean("Teleportation.EnderPearls", true)) {
                        event.setCancelled(true);
                        BattleNight.tellPlayer(player, Track.NO_TP);
                    }
                    break;
                case NETHER_PORTAL:
                case END_PORTAL:
                    if (!BattleNight.config.getBoolean("Teleportation.Portals", false)) {
                        event.setCancelled(true);
                        BattleNight.tellPlayer(player, Track.NO_TP);
                    }
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
    }

    // //////////////////
    // Lounge Events //
    // //////////////////

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!plugin.playersInLounge) return;
        final Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            final Player thrower = (Player) projectile.getShooter();
            if (BattleNight.BattleUsersTeam.containsKey(thrower.getName())) {
                event.setCancelled(true);
                BattleNight.tellPlayer(thrower, Track.NO_CHEATING);
            }
        }
    }
}
