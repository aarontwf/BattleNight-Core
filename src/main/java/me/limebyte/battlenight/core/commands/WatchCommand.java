package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.Battle;
import me.limebyte.battlenight.core.old.Util;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

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

            if (!Util.isSetup()) {
                Messenger.tell(sender, Message.WAYPOINTS_UNSET);
                return false;
            }

            if (!battle.isInProgress()) {
                Messenger.tell(sender, Message.BATTLE_NOT_IN_PROGRESS);
                return false;
            }

            if (battle.spectators.contains(player.getName())) {
                Messenger.tell(sender, Message.ALREADY_SPECTATING);
                return false;
            }

            if (battle.usersTeam.containsKey(player.getName())) {
                Messenger.tell(sender, Message.CANT_SPECTATE);
                return false;
            }

            battle.addSpectator(player, "command");
            return true;
        } else {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
