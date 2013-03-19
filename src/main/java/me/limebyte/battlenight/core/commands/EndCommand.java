package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.core.util.SimpleMessenger.Message;

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
        api.getBattle().stop();
        api.getMessenger().tell(sender, Message.BATTLE_ENDED);
        return true;
    }

}
