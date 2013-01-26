package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AnnounceCommand extends BattleNightCommand {

    protected AnnounceCommand() {
        super("Announce");

        setLabel("announce");
        setDescription("Announces a message to all players in the Battle.");
        setUsage("/bn announce <message>");
        setPermission(CommandPermission.MODERATOR);
        setAliases(Arrays.asList("tellall"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Battle battle = api.getBattle();
        if (!battle.isInProgress()) {
            Messenger.tell(sender, Message.BATTLE_NOT_IN_PROGRESS);
            return false;
        }

        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_MESSAGE);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        Messenger.tellEveryone(ChatColor.translateAlternateColorCodes('&', createString(args, 0)), true);
        return true;
    }

}
