package me.limebyte.battlenight.api.battle;

import org.bukkit.ChatColor;

public interface Team {

    public void addDeath();

    public void addKill();

    public ChatColor getColour();

    public int getDeaths();

    public String getDisplayName();

    public double getKDR();

    public int getKills();

    public String getName();

    public int getSize();

    public boolean isReady();

    public void setColour(ChatColor colour);

    public void setDeaths(int deaths);

    public void setDisplayName(String displayName);

    public void setKills(int kills);

    public void setReady(boolean ready);

}
