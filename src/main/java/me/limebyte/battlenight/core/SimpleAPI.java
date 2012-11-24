package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.Battle;
import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.Util;

public class SimpleAPI implements BattleNightAPI {

    @SuppressWarnings("unused")
    private static BattleNight plugin;

    public SimpleAPI(BattleNight plugin) {
        SimpleAPI.plugin = plugin;
    }

    @Override
    public Battle getBattle() {
        return BattleNight.battle;
    }

    @Override
    public Util getUtil() {
        return BattleNight.util;
    }

}
