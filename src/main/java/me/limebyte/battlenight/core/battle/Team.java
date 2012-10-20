package me.limebyte.battlenight.core.battle;

import org.bukkit.ChatColor;

public enum Team {
    RED("Red", ChatColor.RED),
    BLUE("Blue", ChatColor.BLUE);

    private String name;
    private ChatColor colour;

    Team(String name, ChatColor colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return name;
    }
}
