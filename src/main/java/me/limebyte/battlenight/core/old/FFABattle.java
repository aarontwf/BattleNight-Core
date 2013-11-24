package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.BattleNightAPI;

public class FFABattle extends SimpleBattle {

    public FFABattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        super(api, duration, minPlayers, maxPlayers);
    }

    @Override
    public String getType() {
        return "Free for All";
    }

}
