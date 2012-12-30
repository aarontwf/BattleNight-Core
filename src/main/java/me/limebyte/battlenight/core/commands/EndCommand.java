package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.command.CommandSender;

public class EndCommand extends BattleNightCommand {

    protected EndCommand() {
        super("End");

        setLabel("end");
        setDescription("Ends the Battle.");
        setUsage("/bn end");
        setPermission(CommandPermission.MODERATOR);
        setAliases(Arrays.asList("stop", "kickall"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        BattleNight.getBattle().stop();
        Messaging.tell(sender, Message.BATTLE_ENDED);
        return true;
    }

}
