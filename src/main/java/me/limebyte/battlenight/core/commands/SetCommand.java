package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
                        BattleNight.setCoords(waypoint, player.getLocation());
                        BattleNight.tellPlayer(player, ChatColor.GREEN + waypoint.getDisplayName() + " Waypoint set to your current location."); //TODO
                        return true;
                    } else {
                        sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Please specify a coordinate."); //TODO
                        Messaging.tell(sender, Message.USAGE, getUsage());
                        return false;
                    }
                } else if (args.length == 4 && sender instanceof Player) {
                    Player player = (Player) sender;
                    Location loc = parseArgsToLocation(args, player.getWorld());
                    BattleNight.setCoords(waypoint, loc);
                    BattleNight.tellPlayer(player, ChatColor.GREEN + waypoint.getDisplayName() + " Waypoint set to: " +
                            loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " in this world."); //TODO
                    return true;
                } else if (args.length == 5) {
                    if (Bukkit.getWorld(args[4]) != null) {
                        Location loc = parseArgsToLocation(args);
                        BattleNight.setCoords(waypoint, loc);
                        sender.sendMessage(BattleNight.BNTag + ChatColor.GREEN + waypoint.getDisplayName() + " Waypoint set to: " +
                                loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + " in world " + loc.getWorld().getName() + "."); //TODO
                        return true;
                    } else {
                        sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Can't find world \"" + args[4] + "\"."); //TODO
                        return false;
                    }
                } else {
                    Messaging.tell(sender, Message.INCORRECT_USAGE);
                    Messaging.tell(sender, Message.USAGE, getUsage());
                    return false;
                }
            } else {
                sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Invalid waypoint.  Type \"/bn waypoints\" for a list."); //TODO
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
