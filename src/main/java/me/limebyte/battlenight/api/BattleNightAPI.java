package me.limebyte.battlenight.api;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.PlayerClass;
import me.limebyte.battlenight.api.battle.SpectatorManager;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.util.BattleNightCommand;

import org.bukkit.entity.Player;

public interface BattleNightAPI {

    public Battle getBattle();

    public ArenaManager getArenaManager();

    public BattleManager getBattleManager();

    public SpectatorManager getSpectatorManager();

    public void registerCommand(BattleNightCommand command);

    public void unregisterCommand(BattleNightCommand command);

    public PlayerClass getPlayerClass(Player player);

    public void setPlayerClass(Player player, PlayerClass playerClass);

}
