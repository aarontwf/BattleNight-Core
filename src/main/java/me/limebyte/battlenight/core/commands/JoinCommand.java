package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.Util;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends BattleNightCommand {

    protected JoinCommand() {
        super("Join");

        setLabel("join");
        setDescription("Join the Battle.");
        setUsage("/bn join");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("j", "play"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!Util.isSetup()) {
                Messenger.tell(sender, Message.WAYPOINTS_UNSET);
                return false;
            }

            if (BattleNight.getBattle().isInProgress()) {
                Messenger.tell(sender, Message.BATTLE_IN_PROGRESS);
                return false;
            }

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
                Messenger.tell(sender, Message.ALREADY_IN_BATTLE);
                return false;
            }

            BattleNight.getBattle().addPlayer(player);
            return true;
        } else {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
