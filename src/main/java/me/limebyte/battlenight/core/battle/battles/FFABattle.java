package me.limebyte.battlenight.core.battle.battles;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.battle.SimpleBattle;

public class FFABattle extends SimpleBattle {

    public FFABattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        super(api, duration, minPlayers, maxPlayers);
    }

}
