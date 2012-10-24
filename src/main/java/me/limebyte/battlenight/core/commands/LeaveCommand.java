package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messaging;
import me.limebyte.battlenight.core.util.Messaging.Message;

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

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
                BattleNight.getBattle().removePlayer(player, false, "has left the Battle.", "You have left the Battle.");
                return true;
            } else if (BattleNight.getBattle().spectators.contains(player.getName())) {
                BattleNight.removeSpectator(player);
                return true;
            } else {
                Messaging.tell(sender, Message.NOT_IN_BATTLE);
                return false;
            }
        } else {
            Messaging.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
