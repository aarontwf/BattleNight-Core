package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

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
        Messenger messenger = api.getMessenger();
        Lobby lobby = api.getLobby();

        if (args.length < 1) {
            messenger.tell(sender, Message.SPECIFY_PLAYER);
            messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            String reason = null;

            if (args.length > 1) {
                reason = createString(args, 1);
            }

            if (api.getBattle() != null && api.getBattle().containsPlayer(player)) {
                api.getBattle().removePlayer(player);

                if (reason != null) {
                    messenger.tell(player, Message.REASONED_KICK, reason);
                    messenger.tellBattleExcept(player, Message.PLAYER_REASONED_KICKED, player, reason);
                } else {
                    messenger.tell(player, Message.KICKED);
                    messenger.tellBattleExcept(player, Message.PLAYER_KICKED, player);
                }

                return true;
            } else if (lobby.getPlayers().contains(player.getName())) {
                if (reason != null) {
                    messenger.tell(player, Message.REASONED_KICK, reason);
                    messenger.tellLobby(Message.PLAYER_REASONED_KICKED, player, reason);
                } else {
                    messenger.tell(player, Message.KICKED);
                    messenger.tellLobby(Message.PLAYER_KICKED, player);
                }
                return true;
            } else {
                messenger.tell(sender, Message.PLAYER_NOT_IN_BATTLE, args[0]);
                return false;
            }

        } else {
            messenger.tell(sender, Message.CANT_FIND_PLAYER, args[0]);
            return false;
        }
    }

}
