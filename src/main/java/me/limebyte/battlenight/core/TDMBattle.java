package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.ChatColor;

public class TDMBattle extends TeamedBattle {

    public TDMBattle(int lives) {
        super(lives);
        addTeam(new Team("Blue", ChatColor.BLUE));
        addTeam(new Team("Red", ChatColor.RED));
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
