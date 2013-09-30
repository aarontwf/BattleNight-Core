package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

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
        registerCommand(new ClassCommand());
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
        registerCommand(new VoteCommand());
        registerCommand(new WaypointsCommand());
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

    public static List<String> getMainChoices() {
        List<String> list = new ArrayList<String>();

        for (BattleNightCommand command : commands) {
            list.add(command.getLabel());
        }

        return list;
    }

    public static void registerCommand(BattleNightCommand command) {
        command.api = api;
        commands.add(command);
    }

    public static void unResgisterCommand(BattleNightCommand command) {
        commands.remove(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Messenger messenger = api.getMessenger();

        if (args.length < 1) {
            messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        for (BattleNightCommand command : commands) {
            if (command.labelMatches(args[0]) || command.aliasMatches(args[0])) {
                command.perform(sender, args);
                return true;
            }
        }

        messenger.tell(sender, Message.INVALID_COMMAND);
        return false;
    }

}
