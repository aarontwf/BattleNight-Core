package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.TeamedBattle;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class BattleScorePane extends SimpleScorePane {

    private Battle battle;
    private boolean teamed = false;

    private static final String TITLE_TIME = TITLE_PREFIX + "Battle (%1$TM:%1$TS)";
    private static final String TITLE_NO_TIME = TITLE_PREFIX + "Battle";

    public BattleScorePane(Battle battle, boolean teamed) {
        super();

        this.battle = battle;
        this.teamed = teamed;
    }

    public void addTeam(me.limebyte.battlenight.api.battle.Team team) {
        if (!teamed) return;

        Team t = scoreboard.registerNewTeam("bn_team_" + team.getName());
        t.setDisplayName(team.getDisplayName() + " Team");
        t.setPrefix(team.getColour().toString());

        t.setAllowFriendlyFire(ConfigManager.get(Config.MAIN).getBoolean("FriendlyFire", false));
        t.setCanSeeFriendlyInvisibles(true);
    }

    @Override
    public void addPlayer(Player player) {
        super.addPlayer(player);

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

    public void updateScores(Player player, int score) {
        sidebar.getScore(player).setScore(score);
    }

    public void updateTime(long time) {
        String name = time == -1 ? TITLE_NO_TIME : String.format(TITLE_TIME, time * 1000);
        sidebar.setDisplayName(name);
    }

}
