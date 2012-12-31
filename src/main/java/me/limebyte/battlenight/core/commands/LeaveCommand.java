package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.Battle;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BattleNightCommand {

    protected LeaveCommand() {
        super("Leave");

        setLabel("leave");
        setDescription("Leave the Battle.");
        setUsage("/bn leave");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("l", "quit"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Battle battle = BattleNight.getBattle();
            Player player = (Player) sender;

            if (battle.usersTeam.containsKey(player.getName())) {
                battle.removePlayer(player, false, "has left the Battle.", "You have left the Battle.");
                return true;
            } else if (battle.spectators.contains(player.getName())) {
                battle.removeSpectator(player, null);
                return true;
            } else {
                Messenger.tell(sender, Message.NOT_IN_BATTLE);
                return false;
            }
        } else {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
