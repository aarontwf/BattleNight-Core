package me.limebyte.battlenight.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.core.battle.SimpleLobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class SimpleVote {

    private BattleNightAPI api;
    private Scoreboard scoreboard;
    private Objective sidebar;
    private Map<Arena, Score> votes;
    private Random random;

    public SimpleVote(BattleNightAPI api, SimpleLobby lobby) {
        this.api = api;
        this.scoreboard = lobby.getScoreboard();
        this.sidebar = scoreboard.registerNewObjective("bn_votes", "dummy");
        this.votes = new HashMap<Arena, Score>();
        this.random = new Random();

        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName("" + ChatColor.BOLD + ChatColor.GRAY + "Arena Votes");

        reset();
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return sidebar;
    }

    public Arena getTopArena() {
        int topScore = 0;
        List<Arena> topArenas = new ArrayList<Arena>();

        for (Entry<Arena, Score> entry : votes.entrySet()) {
            int score = entry.getValue().getScore();
            if (score < topScore) continue;
            if (score > topScore) topArenas.clear();
            topArenas.add(entry.getKey());
        }

        int index = random.nextInt(topArenas.size());
        return topArenas.get(index);
    }

    public void reset() {
        for (Arena arena : votes.keySet()) {
            scoreboard.resetScores(Bukkit.getOfflinePlayer(arena.getDisplayName()));
        }

        List<Arena> arenas = api.getArenaManager().getReadyArenas(2);
        for (int i = 0; i < arenas.size(); i++) {
            if (i >= 4) break;
            Arena arena = arenas.get(i);
            Score score = sidebar.getScore(Bukkit.getOfflinePlayer(arena.getDisplayName()));
            score.setScore(1);
            score.setScore(0);
            votes.put(arena, score);
        }
    }

    public Map<Arena, Score> getVotes() {
        return votes;
    }

}
