package me.limebyte.battlenight.api;

import org.bukkit.ChatColor;

public interface Team {

    public String getName();

    public void setName(String name);

    public ChatColor getColour();

    public void setColour(ChatColor colour);

    public boolean isReady();

    public void setReady(boolean ready);

    public int getKills();

    public void setKills(int kills);

    public void addKill();

}
