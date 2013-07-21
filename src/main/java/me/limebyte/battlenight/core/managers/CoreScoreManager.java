package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class CoreScoreManager implements ScoreManager {

    private Scoreboard scoreboard;
    private Objective sidebar;
    private Objective belowName;
    private ScoreboardState state;

    private List<String> players;
    private Map<String, Integer> votes;

    public CoreScoreManager() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("bn_scores", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(state.getTitle());

        belowName = scoreboard.registerNewObjective("bn_belowname", "health");
        if (ConfigManager.get(ConfigManager.Config.MAIN).getBoolean("Scoreboard.DisplayHealth", true)) {
            belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        belowName.setDisplayName(ChatColor.RED + "\u2764");

        players = new ArrayList<String>();
        votes = new HashMap<String, Integer>();
        votes.put("Test", 0);
        votes.put("Arenas", 1);
        votes.put("Go", 2);
        votes.put("Here", 3);
    }

    public void addPlayer(Player player) {
        players.add(player.getName());
        player.setScoreboard(scoreboard);
        scoreboard.resetScores(player);
        player.setHealth(player.getHealth());

        if (state == ScoreboardState.BATTLE) {
            Score score = sidebar.getScore(player);
            score.setScore(1);
            score.setScore(0);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getName());
        player.setScoreboard(scoreboard);
        scoreboard.resetScores(player);

        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) team.removePlayer(player);
    }

    public void addTeam(me.limebyte.battlenight.api.battle.Team team) {
        Team t = scoreboard.registerNewTeam("bn_team_" + team.getName());
        t.setDisplayName(team.getDisplayName() + " Team");
        t.setPrefix(team.getColour().toString());

        t.setAllowFriendlyFire(ConfigManager.get(Config.MAIN).getBoolean("FriendlyFire", false));
        t.setCanSeeFriendlyInvisibles(true);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public ScoreboardState getState() {
        return state;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setState(ScoreboardState state) {
        if (this.state == state) return;
        this.state = state;

        for (OfflinePlayer player : scoreboard.getPlayers()) {
            scoreboard.resetScores(player);
        }

        if (state == ScoreboardState.VOTING) {
            sidebar.setDisplayName(state.getTitle());
            for (Entry<String, Integer> arena : votes.entrySet()) {
                OfflinePlayer vote = Bukkit.getOfflinePlayer(arena.getKey());
                Score score = sidebar.getScore(vote);
                score.setScore(1);
                score.setScore(arena.getValue());
            }
        } else {
            for (String name : players) {
                Player player = Bukkit.getPlayerExact(name);
                if (player == null) continue;

                Score score = sidebar.getScore(player);
                score.setScore(1);
                score.setScore(0);
            }
        }
    }

    public void updateScore(Player player, int score) {
        sidebar.getScore(player).setScore(score);
    }

    public void updateTime(long time) {
        String name = String.format(state.getTitle(), time * 1000);
        sidebar.setDisplayName(name);
    }

}
