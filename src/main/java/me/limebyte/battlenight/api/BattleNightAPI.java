package me.limebyte.battlenight.api;

import java.util.List;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.util.BattleNightCommand;

public interface BattleNightAPI {

    public Battle getBattle();

    public boolean setBattle(Battle battle);

    public void registerCommand(BattleNightCommand command);

    public void unregisterCommand(BattleNightCommand command);

    public void registerArena(Arena arena);

    public void unregisterArena(Arena arena);

    public List<Arena> getArenas();

    public Arena getRandomArena();

    public Waypoint getLoungeWaypoint();

    public Waypoint getExitWaypoint();

}
