package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Battle;

public class FFABattle extends Battle {

    public FFABattle() {
        setBattleLives(8);
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
