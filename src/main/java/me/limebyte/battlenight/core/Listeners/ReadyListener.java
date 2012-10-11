package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Team;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ReadyListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public ReadyListener(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            final Block block = event.getClickedBlock();
            final Player player = event.getPlayer();
            final String name = player.getName();
            if ((block.getTypeId() == plugin.configReadyBlock)
                    && (plugin.BattleUsersTeam.containsKey(name) && (plugin.playersInLounge))
                    && (plugin.teamReady(plugin.BattleUsersTeam.get(player
                            .getName())))) {
                final Team team = plugin.BattleUsersTeam.get(name);

                if (team.equals(Team.RED)) {
                    plugin.redTeamIronClicked = true;
                    plugin.tellEveryone(ChatColor.RED + "Red " + ChatColor.WHITE + "team is ready!");

                    if ((plugin.teamReady(Team.BLUE)) && (plugin.blueTeamIronClicked)) {
                        plugin.playersInLounge = false;
                        plugin.teleportAllToSpawn();
                        plugin.battleInProgress = true;
                        plugin.tellEveryone("Let the Battle begin!");
                    }
                } else if (team.equals(Team.BLUE)) {
                    plugin.blueTeamIronClicked = true;
                    plugin.tellEveryone(ChatColor.BLUE + "Blue " + ChatColor.WHITE + "team is ready!");

                    if ((plugin.teamReady(Team.RED)) && (plugin.redTeamIronClicked)) {
                        plugin.playersInLounge = false;
                        plugin.teleportAllToSpawn();
                        plugin.battleInProgress = true;
                        plugin.tellEveryone("Let the Battle begin!");
                    }
                }
            } else if ((block.getTypeId() == plugin.configReadyBlock) && (plugin.BattleUsersTeam.containsKey(name) && (plugin.playersInLounge))) {
                player.sendMessage(ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE + "Your team have not all picked a class!");
            }
        }
    }
}