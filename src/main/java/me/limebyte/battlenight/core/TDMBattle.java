package me.limebyte.battlenight.core;


import org.bukkit.ChatColor;

public class TDMBattle extends TeamedBattle {

    public TDMBattle(int duration, int minPlayers, int maxPlayers) {
        super(duration, minPlayers, maxPlayers);

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
