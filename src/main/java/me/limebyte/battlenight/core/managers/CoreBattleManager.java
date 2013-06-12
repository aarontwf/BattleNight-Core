package me.limebyte.battlenight.core.managers;

import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.core.battle.battles.FFABattle;
import me.limebyte.battlenight.core.battle.battles.TDMBattle;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

public class CoreBattleManager implements BattleManager {

    private BattleNightAPI api;
    private Battle battle;

    public CoreBattleManager(BattleNightAPI api) {
        this.api = api;

        Battle battle = getNewBattle(getBattleType());
        setBattle(battle != null ? battle : getNewBattle("ffa"));
    }

    @Override
    public Battle getBattle() {
        return battle;
    }

    @Override
    public void setBattle(Battle battle) throws IllegalStateException {
        if (battle != null && battle.isInProgress()) throw new IllegalStateException("Battle in progress!");
        this.battle = battle;
    }

    @Override
    public Battle getNewBattle() {
        Battle battle = getNewBattle(getBattleType());
        setBattle(battle != null ? battle : getNewBattle("FFA"));
        return getBattle();
    }

    @Override
    public Battle getNewBattle(String id) {
        int duration = getDuration();
        int minPlayers = getMinPlayers();
        int maxPlayers = getMaxPlayers();

        if (id.equalsIgnoreCase("FFA")) return new FFABattle(api, duration, minPlayers, maxPlayers);
        if (id.equalsIgnoreCase("TDM")) return new TDMBattle(api, duration, minPlayers, maxPlayers);

        return null;
    }

    private int getDuration() {
        return ConfigManager.get(Config.MAIN).getInt("Battle.Duration", 300);
    }

    private int getMaxPlayers() {
        int maxPlayers = ConfigManager.get(Config.MAIN).getInt("Battle.MaxPlayers", 0);
        int minPlayers = getMinPlayers();
        if (maxPlayers == 0) {
            maxPlayers = Integer.MAX_VALUE;
        }
        return maxPlayers >= minPlayers ? maxPlayers : minPlayers;
    }

    private int getMinPlayers() {
        int minPlayers = ConfigManager.get(Config.MAIN).getInt("Battle.MinPlayers", 2);
        return minPlayers >= 2 ? minPlayers : 2;
    }

    private String getBattleType() {
        return ConfigManager.get(Config.MAIN).getString("Battle.Type", "FFA");
    }

    @Override
    @Deprecated
    public boolean deregister(String id) throws IllegalStateException {
        return false;
    }

    @Override
    @Deprecated
    public Battle getActiveBattle() {
        return getBattle();
    }

    @Override
    @Deprecated
    public Battle getBattle(String id) {
        return null;
    }

    @Override
    @Deprecated
    public List<Battle> getBattles() {
        return null;
    }

    @Override
    @Deprecated
    public void register(Battle battle, String id) throws IllegalArgumentException {
        return;
    }

    @Override
    @Deprecated
    public void reloadBattles() {
        return;
    }

    @Override
    @Deprecated
    public boolean setActiveBattle(String id) throws IllegalStateException {
        return false;
    }
}
