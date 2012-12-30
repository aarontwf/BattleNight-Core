package me.limebyte.battlenight.api;

public class BattleNightAPI {

    private Battle battle;

    public Battle getBattle() {
        return battle;
    }

    public boolean setBattle(Battle battle) {
        if (this.battle.isInProgress()) return false;
        this.battle = battle;
        return true;
    }

}
