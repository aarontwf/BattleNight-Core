package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends BattleNightCommand {

    protected KickCommand() {
        super("Kick");

        setLabel("kick");
        setDescription("Removes the specified player from the Battle.");
        setUsage("/bn kick <player> [reason]");
        setPermission(CommandPermission.MODERATOR);
        setAliases(Arrays.asList("remove", "rm"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_PLAYER);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            if (api.getBattle().containsPlayer(player)) {
                String reason = null;

                if (args.length > 1) {
                    reason = createString(args, 1);
                }

                api.getBattle().removePlayer(player);

                if (reason != null) {
                    Messenger.tell(player, Message.REASONED_KICK, reason);
                    Messenger.tellEveryoneExcept(player, true, Message.PLAYER_REASONED_KICKED, player, reason);
                } else {
                    Messenger.tell(player, Message.KICKED);
                    Messenger.tellEveryoneExcept(player, true, Message.PLAYER_KICKED, player);
                }

                return true;
            } else {
                Messenger.tell(sender, Message.PLAYER_NOT_IN_BATTLE, args[0]);
                return false;
            }

        } else {
            Messenger.tell(sender, Message.CANT_FIND_PLAYER, args[0]);
            return false;
        }
    }

}
