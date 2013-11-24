package me.limebyte.battlenight.core.old;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

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
        Messenger messenger = api.getMessenger();

        if (battle != null && !battle.isInProgress()) {
            messenger.tell(sender, messenger.get("battle.not-running"));
            return false;
        }

        if (args.length < 1) {
            messenger.tell(sender, Message.SPECIFY_MESSAGE);
            messenger.tell(sender, messenger.get("command.usage"), getUsage());
            return false;
        }

        messenger.tellBattle(ChatColor.translateAlternateColorCodes('&', createString(args, 0)));
        return true;
    }

}
