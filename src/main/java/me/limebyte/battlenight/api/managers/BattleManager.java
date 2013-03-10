package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.core.Battle;

public interface BattleManager {

    public void reloadBattles();

    public void register(Battle battle, String id) throws IllegalArgumentException;

    public boolean deregister(String id) throws IllegalStateException;

    public Battle getBattle(String id);

    public List<Battle> getBattles();

    public Battle getActiveBattle();

    public boolean setActiveBattle(String id) throws IllegalStateException;

}
