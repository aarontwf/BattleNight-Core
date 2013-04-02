package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.commands.CommandPermission;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.tosort.SafeTeleporter;
import me.limebyte.battlenight.core.tosort.Waypoint;
import me.limebyte.battlenight.core.util.SimpleMessenger.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand extends BattleNightCommand {

    protected TeleportCommand() {
        super("Teleport");

        setLabel("tp");
        setDescription("Teleport to a waypoint.");
        setUsage("/bn tp <waypoint>\n/bn tp <arena> [spawn]");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("teleport", "goto"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Battle battle = api.getBattle();

            if (battle.containsPlayer(player) || api.getSpectatorManager().getSpectators().contains(player.getName())) {
                messenger.tell(sender, Message.NO_TELEPORTING);
                return false;
            }

            if (args.length < 1) {
                messenger.tell(sender, Message.SPECIFY_WAYPOINT);
                messenger.tell(sender, Message.USAGE, getUsage());
                return false;
            }

            Waypoint waypoint = null;
            if (args[0].equalsIgnoreCase("lounge")) {
                waypoint = api.getArenaManager().getLounge();
            } else if (args[0].equalsIgnoreCase("exit")) {
                waypoint = api.getArenaManager().getExit();
            }

            // TODO Arenas

            if (waypoint == null) {
                messenger.tell(sender, Message.INVALID_WAYPOINT);
                return false;
            }

            if (!waypoint.isSet()) {
                messenger.tell(sender, Message.WAYPOINT_UNSET, waypoint);
                return false;
            }
            SafeTeleporter.tp(player, waypoint);
            return true;
        } else {
            messenger.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }
}
