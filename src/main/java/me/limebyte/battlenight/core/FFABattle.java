package me.limebyte.battlenight.core;


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
