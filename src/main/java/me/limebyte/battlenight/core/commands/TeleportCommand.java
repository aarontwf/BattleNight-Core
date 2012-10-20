package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;
import me.limebyte.battlenight.core.Other.Waypoint;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends BattleNightCommand {

    protected TeleportCommand() {
        super("Teleport");

        this.setLabel("tp");
        this.setDescription("Teleport to a waypoint.");
        this.setUsage("/bn tp <waypoint>");
        this.setPermission(CommandPermission.ADMIN);
        this.setAliases(Arrays.asList("teleport", "goto"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName()) || BattleNight.getBattle().spectators.contains(player.getName())) {
                BattleNight.tellPlayer(player, Track.NO_TP);
                return false;
            }

            if (args.length < 1) {
                BattleNight.tellPlayer(player, ChatColor.RED + "Please specify a waypoint.");
                BattleNight.tellPlayer(player, ChatColor.RED + "Usage: " + getUsage());
                return false;
            }

            Waypoint waypoint = null;
            for (Waypoint wp : Waypoint.values()) {
                if (args[0].equalsIgnoreCase(wp.getName())) {
                    waypoint = wp;
                    break;
                }
            }

            if (waypoint == null) {
                BattleNight.tellPlayer(player, ChatColor.RED + "Invalid waypoint.  Type \"/bn waypoints\" for a list.");
                return false;
            }

            if (!BattleNight.pointSet(waypoint)) {
                BattleNight.tellPlayer(player, ChatColor.RED + "The " + waypoint.getDisplayName() + " waypoint is not set.  No TP.");
                return false;
            }

            BattleNight.goToWaypoint(player, waypoint);
            return true;
        } else {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "This command can only be performed by a player!");
            return false;
        }
    }
}
