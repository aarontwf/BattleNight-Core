package me.limebyte.battlenight.core.battle;

import org.bukkit.ChatColor;

public class SimpleTeam {

    private String name;
    private String displayName;
    private ChatColor colour;
    private boolean ready = false;
    private int kills = 0;
    private int deaths = 0;
    private int size = 0;

    public SimpleTeam(String name) {
        this(name, ChatColor.WHITE);
    }

    public SimpleTeam(String name, ChatColor colour) {
        this.name = name.toLowerCase();
        displayName = name;
        this.colour = colour;
    }

    public void addDeath() {
        deaths++;
    }

    public void addKill() {
        kills++;
    }

    public ChatColor getColour() {
        return colour;
    }

    public int getDeaths() {
        return deaths;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getKDR() {
        if (kills > deaths) {
            if (deaths == 0) return kills;
            return kills / deaths;
        }
        if (kills < deaths) {
            if (kills == 0) return -deaths;
            return 0 - kills / deaths;
        }
        return 0;
    }

    public int getKills() {
        return kills;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean isReady() {
        return ready;
    }

    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return colour + name;
    }

    protected void decrementSize() {
        size--;
    }

    protected void incrementSize() {
        size++;
    }

    protected void reset(SimpleBattle battle) {
        ready = false;
        kills = 0;
        deaths = 0;
        size = 0;
    }

}
