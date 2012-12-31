package me.limebyte.battlenight.api;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.BattleNightCommand;

public interface BattleNightAPI {

    public Battle getBattle();

    public boolean setBattle(Battle battle);

    public abstract void registerCommand(BattleNightCommand command);

    public abstract void unregisterCommand(BattleNightCommand command);

}
