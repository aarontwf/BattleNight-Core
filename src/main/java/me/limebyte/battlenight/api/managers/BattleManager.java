package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.core.SimpleBattle;

public interface BattleManager {

    public boolean deregister(String id) throws IllegalStateException;

    public SimpleBattle getActiveBattle();

    public SimpleBattle getBattle(String id);

    public List<SimpleBattle> getBattles();

    public void register(SimpleBattle battle, String id) throws IllegalArgumentException;

    public void reloadBattles();

    public boolean setActiveBattle(String id) throws IllegalStateException;

}
