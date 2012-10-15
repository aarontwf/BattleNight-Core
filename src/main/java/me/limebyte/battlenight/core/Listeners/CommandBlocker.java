package me.limebyte.battlenight.core.Listeners;

import java.util.List;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public CommandBlocker(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (!BattleNight.BattleUsersTeam.containsKey(event.getPlayer().getName())) return;
        if (!BattleNight.config.getBoolean("Commands.Block")) return;

        final List<String> whitelist = BattleNight.config.getStringList("Commands.Whitelist");
        whitelist.add("bn");

        final String[] cmdArg = event.getMessage().split(" ");
        final String cmdString = cmdArg[0].trim().substring(1).toLowerCase();

        try {
            final Command command = plugin.getServer().getPluginCommand(cmdString);

            // Check if the command is listed
            if (whitelist.contains(command.getLabel().toLowerCase())) return;

            // Check if there are any aliases listed
            if (!command.getAliases().isEmpty()) {
                for (final String alias : command.getAliases()) {
                    if (whitelist.contains(alias.toLowerCase())) return;
                }
            }
        } catch (final NullPointerException e) {
            // Check if the command is listed
            if (whitelist.contains(cmdString)) return;
        }

        // Its not listed so block it
        event.setCancelled(true);
        BattleNight.tellPlayer(event.getPlayer(), "You are not permitted to perform this command while in a Battle.");
        return;
    }

}
