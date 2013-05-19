package me.limebyte.battlenight.core.util;

import java.util.HashMap;
import java.util.Map;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.SimpleBattle;

import org.bukkit.Bukkit;

public class PlayerStats {

    private static Map<String, PlayerStats> players = new HashMap<String, PlayerStats>();

    private String name;

    private int kills;
    private int assists;
    private int killStreak;
    private int deaths;

    private int score;

    public PlayerStats(String name) {
        this.name = name;
        this.kills = 0;
        this.assists = 0;
        this.killStreak = 0;
        this.deaths = 0;
        this.score = 0;
    }

    public static PlayerStats get(String name) {
        synchronized(PlayerStats.class) {
            if (players.get(name) == null) {
                players.put(name, new PlayerStats(name));
            }
            return players.get(name);
        }
    }

    public void addKill(boolean assist) {
        if (assist) {
            assists++;
            score += 5;
        } else {
            kills++;
            score += 10 + killStreak;
        }
        killStreak++;

        SimpleBattle battle = (SimpleBattle) BattleNight.instance.getAPI().getBattleManager().getBattle();
        battle.getScoreboard().updateScores(Bukkit.getPlayerExact(name));
    }

    public void addDeath(boolean suicide) {
        killStreak = 0;
        deaths++;
        if (suicide) score -= 5;
        
        SimpleBattle battle = (SimpleBattle) BattleNight.instance.getAPI().getBattleManager().getBattle();
        battle.getScoreboard().updateScores(Bukkit.getPlayerExact(name));
    }

    public int getKills() {
        return kills;
    }

    public int getAssists() {
        return assists;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        this.kills = 0;
        this.assists = 0;
        this.killStreak = 0;
        this.deaths = 0;
        this.score = 0;
    }

}
