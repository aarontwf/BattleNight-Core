package me.limebyte.battlenight.core.util;

import java.util.HashMap;
import java.util.Map;

import me.limebyte.battlenight.api.battle.ScorePane;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SimpleScorePane implements ScorePane {

    protected Scoreboard scoreboard;
    protected Objective sidebar;
    protected Objective health;

    private static Map<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();

    protected static final String TITLE_PREFIX = ChatColor.BOLD.toString() + ChatColor.GRAY;
    private static final String LOBBY_TITLE = TITLE_PREFIX + "Battle Lobby";

    public SimpleScorePane() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("bn_scores", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(LOBBY_TITLE);

        health = scoreboard.registerNewObjective("bn_belowname", "health");
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);
        health.setDisplayName(ChatColor.RED + "\u2764");
    }

    public void addPlayer(Player player) {
        scoreboards.put(player.getName(), player.getScoreboard());
        player.setScoreboard(scoreboard);

        scoreboard.resetScores(player);

        Score score = sidebar.getScore(player);
        score.setScore(score.getScore() + 1); // Hacky but it works
        score.setScore(0);

        player.setHealth(player.getHealth());
    }

    public void removePlayer(Player player) {
        if (!scoreboard.getPlayers().contains(player)) return;

        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) team.removePlayer(player);

        scoreboard.resetScores(player);

        String name = player.getName();
        Scoreboard board = scoreboards.get(name);
        if (board != null) player.setScoreboard(board);
        scoreboards.remove(name);
    }

}
