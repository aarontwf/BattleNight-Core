package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends BattleNightCommand {

    protected JoinCommand() {
        super("Join");

        this.setLabel("join");
        this.setDescription("Join the Battle.");
        this.setUsage("/bn join");
        this.setPermission(CommandPermission.USER);
        this.setAliases(Arrays.asList("j", "play"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!BattleNight.isSetup()) {
                BattleNight.tellPlayer(player, Track.WAYPOINTS_UNSET);
                return false;
            }

            if (BattleNight.getBattle().isInProgress()) {
                BattleNight.tellPlayer(player, Track.BATTLE_IN_PROGRESS);
                return false;
            }

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
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

}
