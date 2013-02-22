package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.ChatColor;

public class TDMBattle extends TeamedBattle {

    public TDMBattle() {
        setBattleLives(20);
        addTeam(new Team("Red", ChatColor.RED));
        addTeam(new Team("Blue", ChatColor.BLUE));
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
