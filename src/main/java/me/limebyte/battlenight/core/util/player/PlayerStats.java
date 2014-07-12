package me.limebyte.battlenight.core.util.player;

import java.util.UUID;

import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;

public class PlayerStats {

    private UUID uuid;

    private int kills;
    private int assists;
    private int killStreak;
    private int deaths;

    private int score;

    public PlayerStats(UUID uuid) {
        this.uuid = uuid;
        kills = 0;
        assists = 0;
        killStreak = 0;
        deaths = 0;
        score = 0;
    }

    public void addDeath(boolean suicide) {
        killStreak = 0;
        deaths++;
        if (suicide) {
            score -= 5;
        }

        ScoreManager scores = BattleNight.instance.getAPI().getScoreManager();
        scores.updateScore(Bukkit.getPlayer(uuid), score);
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
        scores.updateScore(Bukkit.getPlayer(uuid), score);
    }

    public int getAssists() {
        return assists;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getKillStreak() {
        return killStreak;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        kills = 0;
        assists = 0;
        killStreak = 0;
        deaths = 0;
        score = 0;
    }

}
