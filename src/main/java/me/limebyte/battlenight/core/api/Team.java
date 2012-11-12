package me.limebyte.battlenight.core.api;

import org.bukkit.ChatColor;

public interface Team {

    public String getName();

    public void setName(String name);

    public ChatColor getColour();

    public void setColour(ChatColor colour);

}
