package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.ClassManager;
import me.limebyte.battlenight.api.managers.MusicManager;
import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.PlayerClass;
import me.limebyte.battlenight.core.battle.SimpleLobby;
import me.limebyte.battlenight.core.commands.BattleNightCommand;
import me.limebyte.battlenight.core.commands.CommandManager;
import me.limebyte.battlenight.core.managers.CoreArenaManager;
import me.limebyte.battlenight.core.managers.CoreClassManager;
import me.limebyte.battlenight.core.managers.CoreMusicManager;
import me.limebyte.battlenight.core.managers.CoreScoreManager;
import me.limebyte.battlenight.core.tosort.Metadata;
import me.limebyte.battlenight.core.tosort.PlayerData;
import me.limebyte.battlenight.core.util.SimpleMessenger;

import org.bukkit.entity.Player;

public class API implements BattleNightAPI {

    private Messenger messenger;

    // Managers
    private ArenaManager arenaManager;
    private ClassManager classManager;
    private MusicManager musicManager;
    private ScoreManager scoreboardManager;

    private Lobby lobby;
    private Battle battle;

    public API(BattleNight plugin) {
        messenger = new SimpleMessenger(this);

        // Managers
        arenaManager = new CoreArenaManager(this);
        classManager = new CoreClassManager(this);
        musicManager = new CoreMusicManager(this, plugin);
        scoreboardManager = new CoreScoreManager(this);

        lobby = new SimpleLobby(this);

        PlayerData.api = this;
    }

    @Override
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public ClassManager getClassManager() {
        return classManager;
    }

    @Override
    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public Battle getBattle() {
        return battle;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public MusicManager getMusicManager() {
        return musicManager;
    }

    @Override
    public ScoreManager getScoreManager() {
        return scoreboardManager;
    }

    @Override
    public PlayerClass getPlayerClass(Player player) {
        return Metadata.getPlayerClass(player);
    }

    public void registerCommand(BattleNightCommand command) {
        CommandManager.registerCommand(command);

    }

    @Override
    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    @Override
    public void setPlayerClass(Player player, PlayerClass playerClass) {
        if (playerClass != null) {
            Metadata.set(player, "class", playerClass.getName());
            playerClass.equip(player);
        } else {
            Metadata.remove(player, "class");
        }
    }

    public void unregisterCommand(BattleNightCommand command) {
        CommandManager.unResgisterCommand(command);
    }
}
