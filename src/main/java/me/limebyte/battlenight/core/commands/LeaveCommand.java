package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BattleNightCommand {

    protected LeaveCommand() {
        super("Leave");

        this.setLabel("leave");
        this.setDescription("Leave the Battle.");
        this.setUsage("/bn leave");
        this.setPermission(CommandPermission.USER);
        this.setAliases(Arrays.asList("l", "quit"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
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

}
