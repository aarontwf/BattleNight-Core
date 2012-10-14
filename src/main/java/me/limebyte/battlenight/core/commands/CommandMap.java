package me.limebyte.battlenight.core.commands;

public enum CommandMap {
    DEPRECATED(null, null),
    KICK("kick", CommandPermission.MODERATOR),
    SET("set", CommandPermission.ADMIN),
    VERSION("version", null),
    WAYPOINTS("waypoints", CommandPermission.ADMIN);

    private String command;
    private CommandPermission permission;

    CommandMap(String command, CommandPermission permission) {
        this.command = command;
        this.permission = permission;
    }

    public String getCommand() {
        return command;
    }

    public CommandPermission getPermission() {
        return permission;
    }

}
