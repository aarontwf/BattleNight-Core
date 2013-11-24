package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.ClassManager;
import me.limebyte.battlenight.api.managers.MusicManager;
import me.limebyte.battlenight.api.managers.ScoreManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.PlayerClass;

import org.bukkit.entity.Player;

public class API implements BattleNightAPI {

    private Messenger messenger;

    // Managers
    private ArenaManager arenaManager;
    private ClassManager classManager;
    private MusicManager musicManager;
    private ScoreManager scoreManager;

    private Lobby lobby;
    private Battle battle;

    public API(BattleNight plugin) {
        messenger = new SimpleMessenger(this);

        // Managers
        arenaManager = new CoreArenaManager(this);
        classManager = new CoreClassManager(this);
        musicManager = new CoreMusicManager(this, plugin);
        scoreManager = new CoreScoreManager(this);

        lobby = new SimpleLobby(this);

        PlayerData.api = this;
    }

    @Override
    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public Battle getBattle() {
        return battle;
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
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public MusicManager getMusicManager() {
        return musicManager;
    }

    @Override
    public PlayerClass getPlayerClass(Player player) {
        return Metadata.getPlayerClass(player);
    }

    @Override
    public ScoreManager getScoreManager() {
        return scoreManager;
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
            playerClass.equip(player);
        } else {
            Metadata.remove(player, "class");
        }
    }

    public void unregisterCommand(BattleNightCommand command) {
        CommandManager.unResgisterCommand(command);
    }
}
