package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.command.CommandSender;

public abstract class BattleNightCommand {

    private CommandSender sender;
    private String[] args;

    public BattleNightCommand(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = Arrays.copyOfRange(args, 1, args.length);
    }

    public boolean perform() {
        if (!hasPermission()) return true;
        return onPerformed();
    }

    public abstract boolean onPerformed();

    public CommandSender getSender() {
        return sender;
    }

    public String[] getArgs() {
        return args;
    }

    public abstract CommandPermission getPermission();

    public abstract String getUsage();

    public abstract String getConsoleUsage();

    private boolean hasPermission() {
        if (getPermission() == null) { return true; }

        if ((getPermission().getBukkitPerm() == null) || (getPermission().getBukkitPerm().length() == 0)) { return true; }

        if (BattleNight.config.getBoolean("UsePermissions")) {
            String permission = getPermission().getBukkitPerm();

            if (sender.hasPermission(permission)) {
                return true;
            } else {
                getSender().sendMessage(Track.NO_PERMISSION.msg);
                return false;
            }
        } else {
            if (getPermission().isOpPerm()) {
                if (sender.isOp()) {
                    return true;
                } else {
                    getSender().sendMessage(Track.NO_PERMISSION.msg);
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public String createString(String[] args, int start) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; x++) {
            string.append(args[x]);
            if (x != args.length - 1) {
                string.append(" ");
            }
        }

        return string.toString();
    }

    public int getInteger(String value, int min, int max) {
        int i = min;

        try {
            i = Integer.valueOf(value);
        } catch (final NumberFormatException ex) {
        }

        if (i < min) {
            i = min;
        } else if (i > max) {
            i = max;
        }

        return i;
    }

}
