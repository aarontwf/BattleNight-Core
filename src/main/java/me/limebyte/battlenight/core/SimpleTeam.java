package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.Team;

import org.bukkit.ChatColor;

public class SimpleTeam implements Team {

    private String name;
    private ChatColor colour;
    private boolean ready = false;
    private int kills = 0;

    public SimpleTeam(String name) {
        this(name, ChatColor.WHITE);
    }

    public SimpleTeam(String name, ChatColor colour) {
        this.name = name;
        this.colour = colour;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ChatColor getColour() {
        return colour;
    }

    @Override
    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;

    }

    @Override
    public void addKill() {
        kills++;
    }

}
