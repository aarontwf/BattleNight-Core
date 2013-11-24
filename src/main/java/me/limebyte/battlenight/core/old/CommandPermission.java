package me.limebyte.battlenight.core.old;

public enum CommandPermission {

    USER("battlenight.user", false),
    MODERATOR("battlenight.moderator", true),
    ADMIN("battlenight.admin", true);

    private String permission;
    private boolean op;

    CommandPermission(String permission, boolean op) {
        this.permission = permission;
        this.op = op;
    }

    public String getBukkitPerm() {
        return permission;
    }

    public boolean isOpPerm() {
        return op;
    }
}
