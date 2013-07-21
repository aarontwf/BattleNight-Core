package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;

public class PlayerStats {

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

    public void addKill(boolean assist) {
        int streakBonus = killStreak * killStreak;
        int killScore = 10 + streakBonus;

        if (assist) {
            assists++;
            killScore = killScore / 2;
        } else {
            kills++;
        }

        score = score + killScore;
        killStreak++;

        ScoreManager scores = BattleNight.instance.getAPI().getScoreManager();
        scores.updateScore(Bukkit.getPlayerExact(name), score);
    }

    public void addDeath(boolean suicide) {
        killStreak = 0;
        deaths++;
        if (suicide) score -= 5;

        ScoreManager scores = BattleNight.instance.getAPI().getScoreManager();
        scores.updateScore(Bukkit.getPlayerExact(name), score);
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
