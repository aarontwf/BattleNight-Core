package me.limebyte.battlenight.core.util;

import java.util.HashMap;
import java.util.Map;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class BattleScoreboard {

    private Battle battle;
    private boolean teamed = false;

    private Scoreboard scoreboard;
    private Objective sidebar;
    private Objective belowName;

    private static Map<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();

    private static final String TITLE_PREFIX = ChatColor.BOLD.toString() + ChatColor.GRAY;
    private static final String LOBBY_TITLE = TITLE_PREFIX + "Battle Lobby";
    private static final String BATTLE_TITLE = TITLE_PREFIX + "Battle (%1$TM:%1$TS)";

    public BattleScoreboard(Battle battle) {
        this.battle = battle;
        teamed = battle instanceof TeamedBattle;

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("bn_scores", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName(LOBBY_TITLE);

        belowName = scoreboard.registerNewObjective("bn_belowname", "health");
        belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        belowName.setDisplayName("/ 20");
    }

    public void addTeam(me.limebyte.battlenight.api.battle.Team team, boolean friendlyFire) {
        if (!teamed) return;

        Team t = scoreboard.registerNewTeam("bn_team_" + team.getName());
        t.setDisplayName(team.getDisplayName() + " Team");
        t.setPrefix(team.getColour().toString());

        t.setAllowFriendlyFire(friendlyFire);
        t.setCanSeeFriendlyInvisibles(true);
    }

    public void addPlayer(Player player) {
        scoreboards.put(player.getName(), player.getScoreboard());
        player.setScoreboard(scoreboard);

        scoreboard.resetScores(player);

        Score score = sidebar.getScore(player);
        score.setScore(score.getScore() + 1); // Hacky but it works
        score.setScore(0);

        if (teamed) {
            String teamName = ((TeamedBattle) battle).getTeam(player).getName();
            for (Team team : scoreboard.getTeams()) {
                if (team.getName().equals("bn_team_" + teamName)) {
                    team.addPlayer(player);
                    break;
                }

            }
        }
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

    public void updateScores(Player player) {
        int score = (int) battle.getKDR(player) * 100;
        sidebar.getScore(player).setScore(score);
    }

    public void updateTime(long time) {
        String name = time == -1 ? LOBBY_TITLE : String.format(BATTLE_TITLE, time * 1000);
        sidebar.setDisplayName(name);
    }

}
