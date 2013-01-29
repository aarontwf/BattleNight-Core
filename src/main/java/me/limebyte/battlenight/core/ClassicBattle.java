package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.ChatColor;

public class ClassicBattle extends TeamedBattle {

    public ClassicBattle() {
        setLives(1);
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
