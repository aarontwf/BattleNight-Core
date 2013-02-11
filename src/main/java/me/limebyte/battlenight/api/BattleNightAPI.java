package me.limebyte.battlenight.api;

import java.util.List;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.PlayerClass;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.util.BattleNightCommand;

import org.bukkit.entity.Player;

public interface BattleNightAPI {

    public Battle getBattle();

    public BattleManager getBattleManager();

    public void registerCommand(BattleNightCommand command);

    public void unregisterCommand(BattleNightCommand command);

    public void registerArena(Arena arena);

    public void unregisterArena(Arena arena);

    public List<Arena> getArenas();

    public Arena getRandomArena();

    public Waypoint getLoungeWaypoint();

    public Waypoint getExitWaypoint();

    public PlayerClass getPlayerClass(Player player);

    public void setPlayerClass(Player player, PlayerClass playerClass);

}
