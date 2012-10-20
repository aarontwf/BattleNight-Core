package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messaging;
import me.limebyte.battlenight.core.util.Messaging.Message;

import org.bukkit.command.CommandSender;

public class EndCommand extends BattleNightCommand {

    protected EndCommand() {
        super("End");

        this.setLabel("end");
        this.setDescription("Ends the Battle.");
        this.setUsage("/bn end");
        this.setPermission(CommandPermission.MODERATOR);
        this.setAliases(Arrays.asList("stop", "kickall"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        BattleNight.getBattle().end();
        Messaging.tell(sender, Message.BATTLE_ENDED);
        return true;
    }

}
