package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.commands.CommandManager;

public class API implements BattleNightAPI {

    private Battle battle;

    @Override
    public Battle getBattle() {
        return battle;
    }

    @Override
    public boolean setBattle(Battle battle) {
        if (this.battle != null && this.battle.isInProgress()) return false;
        this.battle = battle;
        return true;
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
    public void registerArena(Arena arena) {
        ArenaManager.register(arena);
    }

    @Override
    public void unregisterArena(Arena arena) {
        ArenaManager.unregister(arena);
    }

}
