package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {
    private static List<BattleNightCommand> commands = new ArrayList<BattleNightCommand>();
    private static BattleNightAPI api;

    public CommandManager(BattleNightAPI api) {
        CommandManager.api = api;
    }

    static {
        commands.add(new AddCommand());
        commands.add(new AnnounceCommand());
        commands.add(new CreateCommand());
        commands.add(new EndCommand());
        commands.add(new HelpCommand());
        commands.add(new JoinCommand());
        commands.add(new KickCommand());
        commands.add(new LeaveCommand());
        commands.add(new ReloadCommand());
        commands.add(new SetCommand());
        commands.add(new TeleportCommand());
        commands.add(new TestCommand());
        commands.add(new VersionCommand());
        commands.add(new WatchCommand());
        commands.add(new WaypointsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            Messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        for (BattleNightCommand command : commands) {
            if (command.labelMatches(args[0]) || command.aliasMatches(args[0])) {
                command.perform(sender, args);
                return true;
            }
        }

        Messenger.tell(sender, Message.INVALID_COMMAND);
        return false;
    }

    public static void registerCommand(BattleNightCommand command) {
        command.api = api;
        commands.add(command);
    }

    public static void unResgisterCommand(BattleNightCommand command) {
        commands.remove(command);
    }

    public static BattleNightCommand getCommand(String name) {
        for (BattleNightCommand cmd : commands) {
            if (cmd.matches(name)) return cmd;
        }

        return null;
    }

    public static List<BattleNightCommand> getCommands() {
        return commands;
    }

}
