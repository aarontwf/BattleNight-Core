package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Battle;
import me.limebyte.battlenight.core.other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WatchCommand extends BattleNightCommand {

    protected WatchCommand() {
        super("Watch");

        this.setLabel("watch");
        this.setDescription("Watch the Battle.");
        this.setUsage("/bn watch");
        this.setPermission(CommandPermission.USER);
        this.setAliases(Arrays.asList("w", "spectate", "view"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Battle battle = BattleNight.getBattle();

            if (!BattleNight.isSetup()) {
                BattleNight.tellPlayer(player, Track.WAYPOINTS_UNSET);
                return false;
            }

            if (!battle.isInProgress()) {
                BattleNight.tellPlayer(player, Track.BATTLE_NOT_IN_PROGRESS);
                return false;
            }

            if (battle.spectators.contains(player.getName())) {
                BattleNight.tellPlayer(player, "You are already watching the Battle!");
                return false;
            }

            if (battle.usersTeam.containsKey(player.getName())) {
                battle.removePlayer(player, false, "has left the Battle.", "You have left the Battle.");
            }

            battle.addSpectator(player, "command");
            return true;
        } else {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "This command can only be performed by a player!");
            return false;
        }
    }

}
