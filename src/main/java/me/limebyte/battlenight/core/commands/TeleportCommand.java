package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.SafeTeleporter;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends BattleNightCommand {

    protected TeleportCommand() {
        super("Teleport");

        setLabel("tp");
        setDescription("Teleport to a waypoint.");
        setUsage("/bn tp <waypoint>");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("teleport", "goto"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (BattleNight.getBattle().usersTeam.containsKey(player.getName()) || BattleNight.getBattle().spectators.contains(player.getName())) {
                Messaging.tell(sender, Message.NO_TELEPORTING);
                return false;
            }

            if (args.length < 1) {
                Messaging.tell(sender, Message.SPECIFY_WAYPOINT);
                Messaging.tell(sender, Message.USAGE, getUsage());
                return false;
            }

            Waypoint waypoint = null;
            for (Waypoint wp : Waypoint.values()) {
                if (args[0].equalsIgnoreCase(wp.getName())) {
                    waypoint = wp;
                    break;
                }
            }

            if (waypoint == null) {
                Messaging.tell(sender, Message.INVALID_WAYPOINT);
                return false;
            }

            if (!waypoint.isSet()) {
                Messaging.tell(sender, Message.WAYPOINT_UNSET, waypoint);
                return false;
            }
            SafeTeleporter.tp(player, waypoint);
            return true;
        } else {
            Messaging.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }
}
