package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging {

    private static final String PREFIX = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;

    /** Strings **/

    public static void tell(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + message);
    }

    public static void tellEveryone(String message) {
        for (String name : BattleNight.getBattle().usersTeam.keySet()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) tell(p, message);
        }
    }

    public static void tellEveryoneExcept(Player player, String message) {
        for (String name : BattleNight.getBattle().usersTeam.keySet()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && p != player) tell(p, message);
        }
    }

    /** Messages **/

    public static void tell(CommandSender sender, Message message) {
        tell(sender, message.getMessage());
    }

    public static void tellEveryone(Message message) {
        tellEveryone(message.getMessage());
    }

    public static void tellEveryoneExcept(Player player, Message message) {
        tellEveryoneExcept(player, message.getMessage());
    }

    public enum Message {
        BATTLE_ENDED("The Battle has ended.");

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
