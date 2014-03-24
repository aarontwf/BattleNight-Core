package org.battlenight.core;

import org.battlenight.api.BattleAPI;
import org.battlenight.api.command.CommandManager;
import org.battlenight.api.configuration.Configuration;
import org.battlenight.api.game.Lobby;
import org.battlenight.api.game.type.GameTypeManager;
import org.battlenight.api.message.Messenger;
import org.battlenight.core.command.SimpleCommandManager;
import org.battlenight.core.configuration.SimpleConfiguration;
import org.battlenight.core.game.SimpleGameTypeManager;
import org.battlenight.core.game.SimpleLobby;
import org.battlenight.core.message.SimpleMessenger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleNight extends JavaPlugin implements BattleAPI {

    private CommandManager commandManager;
    private Configuration configuration;
    private GameTypeManager gameTypeManager;
    private Messenger messenger;
    private Lobby lobby;

    @Override
    public void onEnable() {
        this.commandManager = new SimpleCommandManager(this);
        this.configuration = new SimpleConfiguration(this);
        this.gameTypeManager = new SimpleGameTypeManager(this);
        this.messenger = new SimpleMessenger(this);
        // TODO Loadouts
        this.lobby = new SimpleLobby(this);
        // TODO Maps

        PluginCommand command = getCommand("battlenight");
        command.setExecutor(commandManager);
        command.setTabCompleter(commandManager);

        messenger.logInfo("plugin.enable", getDescription().getVersion());
        messenger.logInfo("Made by LimeByte.");
    }

    @Override
    public void onDisable() {
        messenger.logInfo("plugin.disable", getDescription().getVersion());
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public GameTypeManager getGameTypeManager() {
        return gameTypeManager;
    }

    @Override
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public Lobby getLobby() {
        return lobby;
    }

    @Override
    public void reload() {
        configuration.reload();
        messenger.reload();
    }

}
