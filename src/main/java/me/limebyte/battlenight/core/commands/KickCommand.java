package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends BattleNightCommand {

    protected KickCommand() {
        super("Kick");

        this.setLabel("kick");
        this.setDescription("Removes the specified player from the Battle.");
        this.setUsage("/bn kick <player> [reason]");
        this.setPermission(CommandPermission.MODERATOR);
        this.setAliases(Arrays.asList("remove", "rm"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Please specify a Player.");
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
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

}
