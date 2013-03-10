package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.api.tosort.ListPage;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand extends BattleNightCommand {

    protected HelpCommand() {
        super("Help");

        setLabel("help");
        setDescription("Displays the help menu.");
        setUsage("/bn help");
        setAliases(Arrays.asList("?", "helpme"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        List<String> lines = new ArrayList<String>();
        List<BattleNightCommand> commands = CommandManager.getCommands();

        for (BattleNightCommand cmd : commands) {
            if (cmd.testPermission(sender) && !(cmd instanceof DeprecatedCommand)) {
                lines.add(ChatColor.GRAY + cmd.getUsage() + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + cmd.getDescription());
            }
        }

        ListPage page = new ListPage("BattleNight Help Menu", lines);
        Messenger.tell(sender, page);
        return true;
    }

    // Old Descriptions
    // /bn help - Shows general help.
    // /bn waypoints - Shows set/unset waypoints.
    // /bn version - Shows the version of BattleNight in use.
    // /bn join - Join the Battle.
    // /bn leave - Leave the Battle.
    // /bn watch - Watch the Battle.
    // /bn kick [player] - Kick a player from the Battle.
    // /bn kickall - Kick all players in the Battle.
    // /bn help - Shows general help.
    // /bn version - Shows the version of BattleNight in use.
    // /bn join - Join the Battle.
    // /bn leave - Leave the Battle.
    // /bn watch - Watch the Battle.

}
