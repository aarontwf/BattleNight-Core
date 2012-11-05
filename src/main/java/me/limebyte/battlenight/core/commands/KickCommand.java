package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand extends BattleNightCommand {

    protected KickCommand() {
        super("Kick");

        this.setLabel("kick");
        this.setDescription("Removes the specified player from the Battle.");
        this.setUsage("/bn kick <player> [reason]");
        this.setPermission(CommandPermission.MODERATOR);
        this.setAliases(Arrays.asList("remove", "rm"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            Messaging.tell(sender, Message.SPECIFY_PLAYER);
            Messaging.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        Player player = Bukkit.getPlayerExact(args[0]);

        if (player != null) {
            if (BattleNight.getBattle().usersTeam.containsKey(player.getName())) {
                String reason = null;

                if (args.length > 1) {
                    reason = createString(args, 1);
                }

                if (reason != null) {
                    BattleNight.getBattle().removePlayer(player, false, "has been kicked from the Battle for " + reason + ".", "You have been kicked from the Battle for " + reason + ".");
                } else {
                    BattleNight.getBattle().removePlayer(player, false, "has been kicked from the Battle.", "You have been kicked from the current Battle.");
                }

                return true;
            } else {
                Messaging.tell(sender, Message.PLAYER_NOT_IN_BATTLE, args[0]);
                return false;
            }

        } else {
            Messaging.tell(sender, Message.CANT_FIND_PLAYER, args[0]);
            return false;
        }
    }

}
