package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends BattleNightCommand {

    public JoinCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public boolean onPerformed() {
        CommandSender sender = getSender();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!BattleNight.isSetup()) {
                BattleNight.tellPlayer(player, Track.WAYPOINTS_UNSET);
                return false;
            }

            if (BattleNight.battleInProgress) {
                BattleNight.tellPlayer(player, Track.BATTLE_IN_PROGRESS);
                return false;
            }

            if (BattleNight.BattleUsersTeam.containsKey(player.getName())) {
                BattleNight.tellPlayer(player, Track.ALREADY_IN_TEAM);
                return false;
            }

            BattleNight.getBattle().addPlayer(player);
            return true;
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
        return "/bn join";
    }

    @Override
    public String getConsoleUsage() {
        return null;
    }

}
