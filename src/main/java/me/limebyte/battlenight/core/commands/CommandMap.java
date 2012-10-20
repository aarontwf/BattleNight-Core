package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandMap {
    private static final List<BattleNightCommand> commands = new ArrayList<BattleNightCommand>();

    static {
        commands.add(new AnnounceCommand());
        commands.add(new EndCommand());
        commands.add(new HelpCommand());
        commands.add(new JoinCommand());
        commands.add(new KickCommand());
        commands.add(new LeaveCommand());
        commands.add(new ReloadCommand());
        commands.add(new SetCommand());
        commands.add(new TeleportCommand());
        commands.add(new VersionCommand());
        commands.add(new WatchCommand());
        commands.add(new WaypointsCommand());

        commands.add(new DeprecatedCommand("redlounge", "set redlounge..."));
        commands.add(new DeprecatedCommand("redspawn", "set redspawn..."));
        commands.add(new DeprecatedCommand("bluelounge", "set bluelounge..."));
        commands.add(new DeprecatedCommand("bluespawn", "set bluespawn..."));
        commands.add(new DeprecatedCommand("spectator", "set spectator..."));
        commands.add(new DeprecatedCommand("exit", "set exit..."));
    }

    public static boolean dispatch(CommandSender sender, String[] args) {
        for (BattleNightCommand command : commands) {
            if (command.labelMatches(args[0]) || command.aliasMatches(args[0])) {
                command.perform(sender, args);
                return true;
            }
        }

        sender.sendMessage(BattleNight.BNTag + ChatColor.RED + Track.INVALID_COMAND.msg);
        return false;
    }

    public static BattleNightCommand getCommand(String name) {
        for (BattleNightCommand cmd : commands) {
            if (cmd.matches(name)) { return cmd; }
        }

        return null;
    }

    public static List<BattleNightCommand> getCommands() {
        return commands;
    }

}
