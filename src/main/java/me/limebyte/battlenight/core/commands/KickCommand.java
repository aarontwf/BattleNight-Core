package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends BattleNightCommand {

    public KickCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public boolean onPerformed() {
        CommandSender sender = getSender();
        String[] args = getArgs();

        if (args.length < 1) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Please specify a Player.");
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            if (BattleNight.instance.BattleUsersTeam.containsKey(player.getName())) {
                String reason = null;

                if (args.length > 1) {
                    reason = createString(args, 1);
                }

                if (reason != null) {
                    BattleNight.getBattle().removePlayer(player, false, "has been kicked from the Battle for " + reason + ".", "You have been kicked from the Battle for " + reason + ".");
                } else {
                    BattleNight.getBattle().removePlayer(player, false, "has been kicked from the Battle.", "You have been kicked from the current Battle.");
                }

                return true;
            } else {
                sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Player \"" + args[0] + "\" is not in the Battle.  No kick.");
                return false;
            }

        } else {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Can't find player \"" + args[0] + "\".  No kick.");
            return false;
        }
    }

    @Override
    public String getUsage() {
        return "/bn kick <player> [reason]";
    }

    @Override
    public String getConsoleUsage() {
        return "/bn kick <player> [reason]";
    }

    @Override
    public CommandMap getCommandMap() {
        return CommandMap.KICK;
    }

}
