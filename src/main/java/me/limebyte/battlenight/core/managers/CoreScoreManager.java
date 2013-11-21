package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;
import me.limebyte.battlenight.core.util.player.Metadata;

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

    private BattleNightAPI api;
    private Scoreboard scoreboard;
    private Objective sidebar;
    private Objective belowName;
    private ScoreboardState state;

    private List<String> players;
    private List<Arena> votableArenas;

    public CoreScoreManager(BattleNightAPI api) {
        this.api = api;
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        sidebar = scoreboard.registerNewObjective("bn_scores", "dummy");
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);

        belowName = scoreboard.registerNewObjective("bn_belowname", "health");
        if (ConfigManager.get(ConfigManager.Config.MAIN).getBoolean("Scoreboard.DisplayHealth", true)) {
            belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        belowName.setDisplayName(ChatColor.RED + "\u2764");

        players = new ArrayList<String>();

        setState(ScoreboardState.VOTING);
    }

    @Override
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

    @Override
    public void addTeam(me.limebyte.battlenight.api.battle.Team team) {
        Team t = scoreboard.getTeam("bn_team_" + team.getName());
        if (t == null) {
            t = scoreboard.registerNewTeam("bn_team_" + team.getName());
        }
        t.setDisplayName(team.getDisplayName() + " Team");
        t.setPrefix(team.getColour().toString());

        t.setAllowFriendlyFire(ConfigManager.get(Config.MAIN).getBoolean("FriendlyFire", false));
        t.setCanSeeFriendlyInvisibles(true);
    }

    @Override
    public List<String> getPlayers() {
        return players;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public ScoreboardState getState() {
        return state;
    }

    @Override
    public List<Arena> getVotableArenas() {
        return votableArenas;
    }

    @Override
    public void removePlayer(Player player) {
        players.remove(player.getName());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        scoreboard.resetScores(player);

        if (Metadata.getBoolean(player, "voted")) {
            getVotableArenas().get(Metadata.getInt(player, "vote")).removeVote();
        }

        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) {
            team.removePlayer(player);
        }
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public void setState(ScoreboardState state) {
        if (this.state == state) return;
        this.state = state;

        for (OfflinePlayer player : scoreboard.getPlayers()) {
            scoreboard.resetScores(player);
        }

        sidebar.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + state.getTitle());

        if (state == ScoreboardState.VOTING) {
            updateVotes();
        } else {
            for (String name : players) {
                Player player = Bukkit.getPlayerExact(name);
                if (player == null) {
                    continue;
                }

                Score score = sidebar.getScore(player);
                score.setScore(1);
                score.setScore(0);
            }
        }
    }

    @Override
    public void updateScore(Player player, int score) {
        sidebar.getScore(player).setScore(score);
    }

    @Override
    public void updateTime(long time) {
        String name = String.format(state.getCountdown(), time * 1000);
        sidebar.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + name);
    }

    @Override
    public void updateVotes() {
        if (state != ScoreboardState.VOTING) return;

        List<Arena> arenas = api.getArenaManager().getReadyArenas(1);
        for (int i = 0; i < arenas.size(); i++) {
            Arena arena = arenas.get(i);
            String item = ChatColor.GOLD + "$1." + ChatColor.WHITE + " $2";
            item = api.getMessenger().format(item, i + 1, arena.getDisplayName());
            OfflinePlayer vote = Bukkit.getOfflinePlayer(item.length() > 16 ? item.substring(0, 16) : item);
            Score score = sidebar.getScore(vote);
            score.setScore(1);
            score.setScore(arena.getVotes());
        }
    }
}
