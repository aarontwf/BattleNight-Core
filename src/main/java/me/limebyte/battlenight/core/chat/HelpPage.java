package me.limebyte.battlenight.core.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.commands.BNCommand;
import me.limebyte.battlenight.core.commands.CommandManager;

public class HelpPage extends Page {

    public HelpPage(CommandSender sender) {
        super("BattleNight Help Menu", sender);
    }

    @Override
    public String[] getLines(CommandSender sender) {
        List<String> lines = new ArrayList<String>();
        for (BNCommand command : CommandManager.getCommands()) {
            if(BattleNight.hasPerm(command.getPermission(), sender)) {
                lines.add(command.getDiscription());
            }
        }
        return (String[]) lines.toArray();
    }

}
