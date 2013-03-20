package me.limebyte.battlenight.core.battle.battles;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.battle.SimpleTeam;
import me.limebyte.battlenight.core.battle.SimpleTeamedBattle;

import org.bukkit.ChatColor;

public class TDMBattle extends SimpleTeamedBattle {

    public TDMBattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        super(api, duration, minPlayers, maxPlayers);

        addTeam(new SimpleTeam("Blue", ChatColor.BLUE));
        addTeam(new SimpleTeam("Red", ChatColor.RED));
    }

    @Override
    public boolean onStart() {
        return true;
    }

    @Override
    public boolean onStop() {
        return true;
    }

}
