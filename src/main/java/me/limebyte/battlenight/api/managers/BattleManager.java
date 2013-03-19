package me.limebyte.battlenight.api.managers;

import java.util.List;

import me.limebyte.battlenight.api.battle.Battle;

public interface BattleManager {

    /**
     * DeRegisters a battle with the specified id from the manager effectively
     * removing it.
     * 
     * @param id The id of the battle to remove
     */
    public boolean deregister(String id) throws IllegalStateException;

    public Battle getActiveBattle();

    public Battle getBattle(String id);

    /**
     * Gets the loaded battles.
     * 
     * @return loaded battles
     */
    public List<Battle> getBattles();

    /**
     * Registers a new battle in the manager under the specified id.
     * 
     * @param battle The battle to register
     * @param id The id to assign it
     */
    public void register(Battle battle, String id) throws IllegalArgumentException;

    public void reloadBattles();

    public boolean setActiveBattle(String id) throws IllegalStateException;

}
