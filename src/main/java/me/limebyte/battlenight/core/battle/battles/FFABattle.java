package me.limebyte.battlenight.core.battle.battles;

import me.limebyte.battlenight.core.battle.SimpleBattle;

public class FFABattle extends SimpleBattle {

    public FFABattle(int duration, int minPlayers, int maxPlayers) {
        super(duration, minPlayers, maxPlayers);
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
