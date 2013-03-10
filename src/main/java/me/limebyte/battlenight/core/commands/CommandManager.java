package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.tosort.BattleNightCommand;
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

        registerCommand(new AnnounceCommand());
        registerCommand(new ArenasCommand());
        registerCommand(new EndCommand());
        registerCommand(new HelpCommand());
        registerCommand(new JoinCommand());
        registerCommand(new KickCommand());
        registerCommand(new LeaveCommand());
        registerCommand(new ReloadCommand());
        registerCommand(new SetCommand());
        registerCommand(new TeleportCommand());
        registerCommand(new TestCommand());
        registerCommand(new VersionCommand());
        registerCommand(new WatchCommand());
        registerCommand(new WaypointsCommand());
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
