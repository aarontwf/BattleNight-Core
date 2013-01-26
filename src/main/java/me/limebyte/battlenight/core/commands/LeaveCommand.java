package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.BattleNightCommand;
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
            Battle battle = api.getBattle();
            Player player = (Player) sender;

            if (battle.containsPlayer(player)) {
                return battle.removePlayer(player);
            } else if (battle.containsSpectator(player)) {
                return battle.removeSpectator(player);
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
