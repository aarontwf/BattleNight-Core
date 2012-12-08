package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand extends BattleNightCommand {

    protected SetCommand() {
        super("Set");

        this.setLabel("set");
        this.setDescription("Sets a BattleNight waypoint.");
        this.setUsage("/bn set <waypoint> [x] [y] [z]\n/bn set <waypoint> [x] [y] [z] [world]");
        this.setPermission(CommandPermission.ADMIN);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            Messaging.tell(sender, Message.SPECIFY_WAYPOINT);
            Messaging.tell(sender, Message.USAGE, getUsage());
            return false;
        } else {
            Waypoint waypoint = null;

            for (Waypoint wp : Waypoint.values()) {
                if (args[0].equalsIgnoreCase(wp.getName())) {
                    waypoint = wp;
                    break;
                }
            }

            if (waypoint != null) {
                if (args.length == 1) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        waypoint.setLocation(player.getLocation());
                        Messaging.tell(sender, Message.WAYPOINT_SET_CURRENT_LOC, waypoint);
                        return true;
                    } else {
                        Messaging.tell(sender, Message.SPECIFY_COORDINATE);
                        Messaging.tell(sender, Message.USAGE, getUsage());
                        return false;
                    }
                } else if (args.length == 4 && sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = parseArgsToLocation(args, player.getWorld());
                    waypoint.setLocation(loc);
                    Messaging.tell(sender, Message.WAYPOINT_SET_THIS_WORLD, waypoint, loc);
                    return true;
                } else if (args.length == 5) {
                    if (Bukkit.getWorld(args[4]) != null) {
                        Location loc = parseArgsToLocation(args);
                        waypoint.setLocation(loc);
                        Messaging.tell(sender, Message.WAYPOINT_SET, waypoint, loc, loc.getWorld());
                        return true;
                    } else {
                        Messaging.tell(sender, Message.CANT_FIND_WORLD, args[4]);
                        return false;
                    }
                } else {
                    Messaging.tell(sender, Message.INCORRECT_USAGE);
                    Messaging.tell(sender, Message.USAGE, getUsage());
                    return false;
                }
            } else {
                Messaging.tell(sender, Message.INVALID_WAYPOINT);
                return false;
            }
        }
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

}
