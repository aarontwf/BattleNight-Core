package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BattleNightCommand {

    public LeaveCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public boolean onPerformed() {
        CommandSender sender = getSender();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (BattleNight.BattleUsersTeam.containsKey(player.getName())) {
                BattleNight.getBattle().removePlayer(player, false, "has left the Battle.", "You have left the Battle.");
                return true;
            } else if (BattleNight.BattleSpectators.containsKey(player.getName())) {
                BattleNight.removeSpectator(player);
                return true;
            } else {
                BattleNight.tellPlayer(player, Track.NOT_IN_TEAM);
                return false;
            }
        } else {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "This command can only be performed by a player!");
            return false;
        }
    }

    @Override
    public CommandPermission getPermission() {
        return CommandPermission.USER;
    }

    @Override
    public String getUsage() {
        return "/bn leave";
    }

    @Override
    public String getConsoleUsage() {
        return null;
    }

}
