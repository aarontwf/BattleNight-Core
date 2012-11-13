package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.other.Tracks.Track;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CheatListener implements Listener {

    // //////////////////
    // General Events //
    // //////////////////

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        if (BattleNight.getBattle().usersTeam.containsKey(player.getName()) && !BattleNight.BattleTelePass.containsKey(player.getName())) {
            switch (event.getCause()) {
                case COMMAND:
                case PLUGIN:
                    event.setCancelled(true);
                    BattleNight.tellPlayer(player, Track.NO_TP);
                    break;
                case ENDER_PEARL:
                    if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.EnderPearls", true)) {
                        event.setCancelled(true);
                        BattleNight.tellPlayer(player, Track.NO_TP);
                    }
                    break;
                case NETHER_PORTAL:
                case END_PORTAL:
                    if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.Portals", false)) {
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE + "Not so fast! No Cheating!");
        }
    }

    // //////////////////
    // Lounge Events //
    // //////////////////

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!BattleNight.getBattle().isInLounge()) return;
        final Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            final Player thrower = (Player) projectile.getShooter();
            if (BattleNight.getBattle().usersTeam.containsKey(thrower.getName())) {
                event.setCancelled(true);
                BattleNight.tellPlayer(thrower, Track.NO_CHEATING);
            }
        }
    }
}
