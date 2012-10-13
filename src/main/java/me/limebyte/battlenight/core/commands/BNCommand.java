package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.command.CommandSender;

public abstract class BNCommand {

    private CommandSender sender;
    private String[] args;

    public BNCommand(CommandSender sender, String[] args) {
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

        String permission = getPermission().getBukkitPerm();

        if (sender.hasPermission(permission)) {
            return true;
        } else {
            getSender().sendMessage(Track.NO_PERMISSION.msg);
            return false;
        }
    }

}
