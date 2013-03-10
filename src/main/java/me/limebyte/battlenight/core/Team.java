package me.limebyte.battlenight.core;


import org.bukkit.ChatColor;

public class Team {

    private String name;
    private String displayName;
    private ChatColor colour;
    private boolean ready = false;
    private int kills = 0;
    private int deaths = 0;
    private int size = 0;

    public Team(String name) {
        this(name, ChatColor.WHITE);
    }

    public Team(String name, ChatColor colour) {
        this.name = name.toLowerCase();
        this.displayName = name;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ChatColor getColour() {
        return colour;
    }

    public void setColour(ChatColor colour) {
        this.colour = colour;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void addKill() {
        kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public double getKD() {
        if (kills > deaths) {
            if (deaths == 0) return kills;
            return kills / deaths;
        }
        if (kills < deaths) {
            if (kills == 0) return -deaths;
            return 0 - (kills / deaths);
        }
        return 0;
    }

    public int getSize() {
        return size;
    }

    protected void incrementSize() {
        size++;
    }

    protected void decrementSize() {
        size--;
    }

    @Override
    public String toString() {
        return colour + name;
    }

    protected void reset(Battle battle) {
        ready = false;
        kills = 0;
        deaths = 0;
        size = 0;
    }

}
