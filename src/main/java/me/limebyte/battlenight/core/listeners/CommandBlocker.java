package me.limebyte.battlenight.core.listeners;

import java.util.List;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (!BattleNight.getBattle().usersTeam.containsKey(event.getPlayer().getName())) return;
        if (!ConfigManager.get(Config.MAIN).getBoolean("Commands.Block", true)) return;

        List<String> whitelist = ConfigManager.get(Config.MAIN).getStringList("Commands.Whitelist");
        whitelist.add("bn");

        String[] cmdArg = event.getMessage().split(" ");
        String cmdString = cmdArg[0].trim().substring(1).toLowerCase();

        try {
            Command command = Bukkit.getServer().getPluginCommand(cmdString);

            // Check if the command is listed
            if (whitelist.contains(command.getLabel().toLowerCase())) return;

            // Check if there are any aliases listed
            if (!command.getAliases().isEmpty()) {
                for (String alias : command.getAliases()) {
                    if (whitelist.contains(alias.toLowerCase())) return;
                }
            }
        } catch (NullPointerException e) {
            // Check if the command is listed
            if (whitelist.contains(cmdString)) return;
        }

        // Its not listed so block it
        event.setCancelled(true);
        BattleNight.tellPlayer(event.getPlayer(), "You are not permitted to perform this command while in a Battle.");
        return;
    }

}
