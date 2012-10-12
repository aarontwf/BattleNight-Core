package me.limebyte.battlenight.core.commands;

public enum CommandPermission {

    USER("battlenight.user"),
    MODERATOR("battlenight.moderator"),
    ADMIN("battlenight.admin");

    private String permission;

    CommandPermission(String permission) {
        this.permission = permission;
    }

    public String getBukkitPerm() {
        return permission;
    }

}
