package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.api.battle.Battle;

public interface BattleManager {

    public void registerBattle(Battle battle, String id) throws IllegalArgumentException;

    public boolean deregisterBattle(String id) throws IllegalStateException;

    public Battle getBattle(String id);

    public List<Battle> getBattles();

    public Battle getActiveBattle();

    public boolean setActiveBattle(String id) throws IllegalStateException;

    public void reload();

}
