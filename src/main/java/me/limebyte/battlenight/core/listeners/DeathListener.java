package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public DeathListener(BattleNight instance) {
        plugin = instance;
    }

    // Called when player dies
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        final Entity e = event.getEntity();
        if (e instanceof Player) {
            final Player player = (Player) e;
            final String name = player.getName();
            if (BattleNight.getBattle().usersTeam.containsKey(name)) {
                event.getDrops().clear();
                ((PlayerDeathEvent) event).setDeathMessage("");

                if (!plugin.playersInLounge) {
                    try {
                        final Player killer = player.getKiller();
                        String playerName;
                        String killerName;

                        // Colour Names
                        if (isInTeam(killer, Team.BLUE))
                            killerName = ChatColor.BLUE + killer.getName();
                        else if (isInTeam(killer, Team.RED))
                            killerName = ChatColor.RED + killer.getName();
                        else killerName = ChatColor.BLACK + killer.getName();

                        if (isInTeam(player, Team.BLUE))
                            playerName = ChatColor.BLUE + player.getName();
                        else if (isInTeam(player, Team.RED))
                            playerName = ChatColor.RED + player.getName();
                        else playerName = ChatColor.BLACK + player.getName();
                        // ------------

                        plugin.killFeed(killerName + ChatColor.GRAY
                                + " killed " + playerName + ".");
                    } catch (final NullPointerException error) {
                        plugin.killFeed(ChatColor.RED + name + ChatColor.GRAY + " was killed.");
                        if (BattleNight.configDebug) {
                            BattleNight.log.warning("Could not find killer for player: " + name);
                        }
                    }
                }

                plugin.BattleUsersRespawn.put(name, "true");
                BattleNight.battle.removePlayer(player, true, null, "You were killed!");
            }
        }
    }

    private boolean isInTeam(Player player, Team team) {
        final String name = player.getName();
        if (BattleNight.getBattle().usersTeam.containsKey(name)) {
            if ((BattleNight.getBattle().usersTeam.get(name).equals(team))) { return true; }
        }

        return false;
    }
}