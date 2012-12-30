package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Battle;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WatchCommand extends BattleNightCommand {

    protected WatchCommand() {
        super("Watch");

        setLabel("watch");
        setDescription("Watch the Battle.");
        setUsage("/bn watch");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("w", "spectate", "view"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Battle battle = BattleNight.getBattle();

            if (!BattleNight.isSetup()) {
                Messaging.tell(sender, Message.WAYPOINTS_UNSET);
                return false;
            }

            if (!battle.isInProgress()) {
                Messaging.tell(sender, Message.BATTLE_NOT_IN_PROGRESS);
                return false;
            }

            if (battle.spectators.contains(player.getName())) {
                Messaging.tell(sender, Message.ALREADY_SPECTATING);
                return false;
            }

            if (battle.usersTeam.containsKey(player.getName())) {
                Messaging.tell(sender, Message.CANT_SPECTATE);
                return false;
            }

            battle.addSpectator(player, "command");
            return true;
        } else {
            Messaging.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
