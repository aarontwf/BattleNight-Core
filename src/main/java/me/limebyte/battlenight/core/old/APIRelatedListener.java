package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.BattleNightAPI;

import org.bukkit.event.Listener;

public class APIRelatedListener implements Listener {

    private BattleNightAPI api;

    public APIRelatedListener(BattleNightAPI api) {
        this.api = api;
    }

    protected BattleNightAPI getAPI() {
        return api;
    }

}
