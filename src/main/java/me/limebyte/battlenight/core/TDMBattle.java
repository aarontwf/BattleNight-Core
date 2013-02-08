package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.ChatColor;

public class TDMBattle extends TeamedBattle {

    public TDMBattle() {
        setBattleLives(30);
        addTeam(new Team("A", ChatColor.AQUA));
        addTeam(new Team("B", ChatColor.GREEN));
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
