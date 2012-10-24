package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messaging;
import me.limebyte.battlenight.core.util.Messaging.Message;

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
                Messaging.tell(sender, Message.WAYPOINTS_UNSET);
                return false;
            }

            if (BattleNight.getBattle().isInProgress()) {
                Messaging.tell(sender, Message.BATTLE_IN_PROGRESS);
                return false;
            }

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
                Messaging.tell(sender, Message.ALREADY_IN_BATTLE);
                return false;
            }

            BattleNight.getBattle().addPlayer(player);
            return true;
        } else {
            Messaging.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
