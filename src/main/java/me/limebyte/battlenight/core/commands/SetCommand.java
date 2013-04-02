package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.tosort.Waypoint;
import me.limebyte.battlenight.core.util.SimpleMessenger.Message;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends BattleNightCommand {

    protected SetCommand() {
        super("Set");

        setLabel("set");
        setDescription("Sets a BattleNight waypoint.");
        setUsage("/bn set <waypoint> [x] [y] [z] [world]");
        setPermission(CommandPermission.ADMIN);
    }

    private Location parseArgsToLocation(String[] args) {
        int x = getInteger(args[1], -30000000, 30000000);
        int y = getInteger(args[2], 0, 256);
        int z = getInteger(args[3], -30000000, 30000000);
        World world = Bukkit.getWorld(args[4]);

        return new Location(world, x + 0.5, y, z + 0.5);
    }

    private Location parseArgsToLocation(String[] args, World world) {
        int x = getInteger(args[1], -30000000, 30000000);
        int y = getInteger(args[2], 0, 256);
        int z = getInteger(args[3], -30000000, 30000000);

        return new Location(world, x + 0.5, y, z + 0.5);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        if (args.length < 1) {
            messenger.tell(sender, Message.SPECIFY_WAYPOINT);
            messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        } else {
            Waypoint waypoint = null;
            String name = "";
            ArenaManager manager = api.getArenaManager();

            if (args[0].equalsIgnoreCase("lounge")) {
                waypoint = manager.getLounge();
                name = "Lounge";
            } else if (args[0].equalsIgnoreCase("exit")) {
                waypoint = manager.getExit();
                name = "Exit";
            }

            if (waypoint != null) {
                if (args.length == 1) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        waypoint.setLocation(player.getLocation());
                        messenger.tell(sender, Message.WAYPOINT_SET_CURRENT_LOC, name);
                    } else {
                        messenger.tell(sender, Message.SPECIFY_COORDINATE);
                        messenger.tell(sender, Message.USAGE, getUsage());
                        return false;
                    }
                } else if (args.length == 4 && sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = parseArgsToLocation(args, player.getWorld());
                    waypoint.setLocation(loc);
                    messenger.tell(sender, Message.WAYPOINT_SET_THIS_WORLD, name, loc);
                } else if (args.length == 5) {
                    if (Bukkit.getWorld(args[4]) != null) {
                        Location loc = parseArgsToLocation(args);
                        waypoint.setLocation(loc);
                        messenger.tell(sender, Message.WAYPOINT_SET, name, loc, loc.getWorld());
                    } else {
                        messenger.tell(sender, Message.CANT_FIND_WORLD, args[4]);
                        return false;
                    }
                } else {
                    messenger.tell(sender, Message.INCORRECT_USAGE);
                    messenger.tell(sender, Message.USAGE, getUsage());
                    return false;
                }
                return true;
            } else {
                messenger.tell(sender, Message.INVALID_WAYPOINT);
                return false;
            }
        }
    }

}
