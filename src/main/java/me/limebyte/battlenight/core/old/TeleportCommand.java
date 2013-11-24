package me.limebyte.battlenight.core.old;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class TeleportCommand extends BattleNightCommand {

    protected TeleportCommand() {
        super("Teleport");

        setLabel("tp");
        setDescription("Teleport to a waypoint.");
        setUsage("/bn tp <waypoint>\n/bn tp <arena> [spawn]");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("teleport", "goto"));
        setPrimaryChoices(ImmutableList.of("lounge", "exit"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Battle battle = api.getBattle();

            if (battle != null && battle.containsPlayer(player)) {
                messenger.tell(player, messenger.get("general.no-teleport"));
                return false;
            }

            if (args.length < 1) {
                messenger.tell(sender, Message.SPECIFY_WAYPOINT);
                messenger.tell(sender, messenger.get("command.usage"), getUsage());
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
            Teleporter.tp(player, waypoint);
            return true;
        } else {
            messenger.tell(sender, messenger.get("command.player-only"));
            return false;
        }
    }
}
