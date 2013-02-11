package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.core.FFABattle;
import me.limebyte.battlenight.core.TDMBattle;

public class CoreBattleManager implements BattleManager {

    private BattleNightAPI api;
    private String activeBattle;
    private Map<String, Battle> battles = new HashMap<String, Battle>();

    public CoreBattleManager(BattleNightAPI api) {
        this.api = api;

        // Defaults
        registerBattle(new TDMBattle(), "TDM");
        registerBattle(new FFABattle(), "FFA");
    }

    @Override
    public void registerBattle(Battle battle, String id) {
        if (battle == null || battles.containsKey(id)) throw new IllegalArgumentException();
        battle.api = api;
        battles.put(id, battle);
    }

    @Override
    public boolean deregisterBattle(String id) {
        if (activeBattle.equals(id)) throw new IllegalStateException();
        if (!battles.containsKey(id)) return false;
        battles.remove(id);
        return true;
    }

    @Override
    public Battle getBattle(String id) {
        return battles.get(id);
    }

    @Override
    public List<Battle> getBattles() {
        return new ArrayList<Battle>(battles.values());
    }

    @Override
    public Battle getActiveBattle() {
        return getBattle(activeBattle);
    }

    @Override
    public boolean setActiveBattle(String id) {
        if (!battles.containsKey(id)) return false;

        Battle active = getActiveBattle();
        if (active != null && active.isInProgress()) throw new IllegalStateException();

        activeBattle = id;
        return true;
    }

}
