package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.StandardBattle;

public class FFABattle extends StandardBattle {

    public FFABattle() {
        setLives(5);
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
