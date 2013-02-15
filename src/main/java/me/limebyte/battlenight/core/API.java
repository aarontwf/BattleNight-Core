package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.PlayerClass;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.commands.CommandManager;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.managers.CoreArenaManager;
import me.limebyte.battlenight.core.managers.CoreBattleManager;
import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.entity.Player;

public class API implements BattleNightAPI {

    private BattleManager battleManager;
    private ArenaManager arenaManager;

    public API() {
        battleManager = new CoreBattleManager(this);
        arenaManager = new CoreArenaManager();
        PlayerData.api = this;
    }

    @Override
    public Battle getBattle() {
        return battleManager.getActiveBattle();
    }

    @Override
    public BattleManager getBattleManager() {
        return battleManager;
    }

    @Override
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public void registerCommand(BattleNightCommand command) {
        CommandManager.registerCommand(command);

    }

    @Override
    public void unregisterCommand(BattleNightCommand command) {
        CommandManager.unResgisterCommand(command);
    }

    @Override
    public PlayerClass getPlayerClass(Player player) {
        return Metadata.getBattleClass(player);
    }

    @Override
    public void setPlayerClass(Player player, PlayerClass playerClass) {
        if (playerClass != null) {
            Metadata.set(player, "class", playerClass.getName());
            playerClass.equip(player);
        } else {
            Metadata.remove(player, "class");
            SignListener.cleanSigns(player);
        }
    }
}
