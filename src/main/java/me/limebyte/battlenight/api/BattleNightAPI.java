package me.limebyte.battlenight.api;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.managers.ClassManager;
import me.limebyte.battlenight.api.managers.MusicManager;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.util.PlayerClass;

import org.bukkit.entity.Player;

public interface BattleNightAPI {

    public ArenaManager getArenaManager();

    public Battle getBattle();

    public BattleManager getBattleManager();

    public ClassManager getClassManager();

    public MusicManager getMusicManager();

    public PlayerClass getPlayerClass(Player player);

    public SpectatorManager getSpectatorManager();

    public void registerCommand(BattleNightCommand command);

    public void setPlayerClass(Player player, PlayerClass playerClass);

    public void unregisterCommand(BattleNightCommand command);

}
