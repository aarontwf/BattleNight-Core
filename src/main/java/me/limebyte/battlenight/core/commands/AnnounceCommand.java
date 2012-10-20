package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AnnounceCommand extends BattleNightCommand {

    protected AnnounceCommand() {
        super("Announce");

        this.setLabel("announce");
        this.setDescription("Announces a message to all player in the Battle.");
        this.setUsage("/bn announce <message>");
        this.setPermission(CommandPermission.MODERATOR);
        this.setAliases(Arrays.asList("tellall"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (!BattleNight.battleInProgress) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + Track.BATTLE_NOT_IN_PROGRESS.msg);
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Please specify a message.");
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage());
            return false;
        }

        BattleNight.tellEveryone(ChatColor.translateAlternateColorCodes('&', args[0]));
        return true;
    }

}
