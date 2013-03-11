package me.limebyte.battlenight.api;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.managers.ClassManager;
import me.limebyte.battlenight.api.managers.MusicManager;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.api.util.PlayerClass;

import org.bukkit.entity.Player;

public interface BattleNightAPI {

    public Battle getBattle();

    public ArenaManager getArenaManager();

    public BattleManager getBattleManager();

    public ClassManager getClassManager();

    public MusicManager getMusicManager();

    public SpectatorManager getSpectatorManager();

    public void registerCommand(BattleNightCommand command);

    public void unregisterCommand(BattleNightCommand command);

    public PlayerClass getPlayerClass(Player player);

    public void setPlayerClass(Player player, PlayerClass playerClass);

}
