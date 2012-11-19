package me.limebyte.battlenight.core.util.chat;

import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Messaging {

    public static final String PREFIX = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;

    /** Strings **/

    public static void tell(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + message);
    }

    public static void tellEveryone(String message, boolean spectators) {
        if (spectators) {
            for (String name : BattleNight.getBattle().spectators) {
                Player p = Bukkit.getPlayerExact(name);
                if (p != null) tell(p, message);
            }
        }

        for (String name : BattleNight.getBattle().usersTeam.keySet()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) tell(p, message);
        }
    }

    public static void tellEveryoneExcept(Player player, String message, boolean spectators) {
        if (spectators) {
            for (String name : BattleNight.getBattle().spectators) {
                Player p = Bukkit.getPlayerExact(name);
                if (p != null && player != p) tell(p, message);
            }
        }

        for (String name : BattleNight.getBattle().usersTeam.keySet()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && player != p) tell(p, message);
        }
    }

    public static void log(Level level, String message) {
        BattleNight.getInstance().getLogger().log(level, message);
    }

    public static void debug(Level level, String message) {
        if (ConfigManager.get(Config.MAIN).getBoolean("Debug", false)) {
            log(level, message);
        }
    }

    /** Messages **/

    public static void tell(CommandSender sender, Message message) {
        tell(sender, message.getMessage());
    }

    public static void tell(CommandSender sender, Message message, Object... args) {
        String msg = "";

        for (int i = 0; i < args.length; i++) {
            msg = message.getMessage().replace("$" + (i + 1), describeObject(args[i]));
        }

        tell(sender, msg);
    }

    public static void tellEveryone(Message message, boolean spectators) {
        tellEveryone(message.getMessage(), spectators);
    }

    public static void tellEveryone(Message message, boolean spectators, Object... args) {
        String msg = "";

        for (int i = 0; i < args.length; i++) {
            msg = message.getMessage().replace("$" + (i + 1), describeObject(args[i]));
        }

        tellEveryone(msg, spectators);
    }

    public static void tellEveryoneExcept(Player player, Message message, boolean spectators) {
        tellEveryoneExcept(player, message.getMessage(), spectators);
    }

    public static void tellEveryoneExcept(Player player, Message message, boolean spectators, Object... args) {
        String msg = "";

        for (int i = 0; i < args.length; i++) {
            msg = message.getMessage().replace("$" + (i + 1), describeObject(args[i]));
        }

        tellEveryoneExcept(player, msg, spectators);
    }

    private static String describeObject(Object obj) {
        if (obj instanceof ComplexEntityPart) { // Complex entities
            return describeObject(((ComplexEntityPart) obj).getParent());
        } else if (obj instanceof Item) { // Dropped items
            return describeMaterial(((Item) obj).getItemStack().getType());
        } else if (obj instanceof ItemStack) { // Items
            return describeMaterial(((ItemStack) obj).getType());
        } else if (obj instanceof Entity) { // Entities
            return describeEntity((Entity) obj);
        } else if (obj instanceof Block) { // Blocks
            return describeMaterial(((Block) obj).getType());
        } else if (obj instanceof Material) { // Just material
            return describeMaterial((Material) obj);
        }

        return obj.toString();
    }

    private static String describeEntity(Entity entity) {
        if (entity instanceof Player) { return ((Player) entity).getName(); }

        return entity.getType().toString().toLowerCase().replace("_", " ");
    }

    private static String describeMaterial(Material material) {
        if (material == Material.INK_SACK) { return "dye"; }

        return material.toString().toLowerCase().replace("_", " ");
    }

    public enum Message {
        SUCCESSFUL_SIGN(ChatColor.GREEN + "Successfully created sign for $1!"),
        UNSUCCESSFUL_SIGN(ChatColor.RED + "Error creating sign for $1!  Leave last 3 lines blank."),

        SPECIFY_MESSAGE(ChatColor.RED + "Please specify a message."),
        SPECIFY_PLAYER(ChatColor.RED + "Please specify a player."),
        SPECIFY_WAYPOINT(ChatColor.RED + "Please specify a waypoint."),
        SPECIFY_TEST(ChatColor.RED + "Please specify a test."),
        USAGE("Usage: $1"),
        NO_PERMISSION(ChatColor.RED + "You do not have permission to use this command."),
        PLAYER_ONLY(ChatColor.RED + "This command can only be performed by a player!"),
        INVALID_COMMAND(ChatColor.RED + "Invalid Command.  Type '/bn help' for help."),
        INCORRECT_USAGE(ChatColor.RED + "Incorrect usage."),
        DEPRICATED_COMMAND(ChatColor.RED + "This command is deprecated, please use \"/bn $1\" instead."),
        WAYPOINTS_UNSET(ChatColor.RED + "All waypoints must be set up first."),
        ALREADY_IN_BATTLE(ChatColor.RED + "You have already joined a Battle!"),
        NOT_IN_BATTLE(ChatColor.RED + "You are not in a Battle."),
        PLAYER_NOT_IN_BATTLE(ChatColor.RED + "Player \"$1\" is not in the Battle."),
        CANT_FIND_PLAYER(ChatColor.RED + "Can't find player \"$1\"."),

        BATTLE_IN_PROGRESS(ChatColor.RED + "A game is already in progress."),
        BATTLE_NOT_IN_PROGRESS(ChatColor.RED + "A Battle is not currently in progress."),

        TEAM_IS_READY("$1" + ChatColor.WHITE + " Team is ready!"),

        BATTLE_STARTED(ChatColor.GREEN + "Let the Battle begin!"),
        BATTLE_ENDED("The Battle has ended."),

        TEAM_WON("$1 Team won the Battle!"),
        DRAW(ChatColor.DARK_PURPLE + "Draw!"),

        RELOADING("Reloading BattleNight..."),
        RELOAD_SUCCESSFUL(ChatColor.GREEN + "Reloaded successfully."),
        RELOAD_FAILED(ChatColor.RED + "Reload failed.  See console for error log.");

        private String message;

        Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

}
