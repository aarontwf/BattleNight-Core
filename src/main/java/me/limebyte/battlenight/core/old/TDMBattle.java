package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.BattleNightAPI;

import org.bukkit.ChatColor;

public class TDMBattle extends SimpleTeamedBattle {

    public TDMBattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        super(api, duration, minPlayers, maxPlayers);

        addTeam(new SimpleTeam("Blue", ChatColor.BLUE));
        addTeam(new SimpleTeam("Red", ChatColor.RED));
    }

    @Override
    public String getType() {
        return "Team Deathmatch";
    }

}
