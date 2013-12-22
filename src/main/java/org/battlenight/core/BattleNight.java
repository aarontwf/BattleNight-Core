package org.battlenight.core;

import org.battlenight.api.BattleAPI;
import org.battlenight.api.command.CommandManager;
import org.battlenight.api.configuration.Configuration;
import org.battlenight.api.message.Messenger;
import org.battlenight.core.command.SimpleCommandManager;
import org.battlenight.core.configuration.SimpleConfiguration;
import org.battlenight.core.message.SimpleMessenger;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleNight extends JavaPlugin implements BattleAPI {

    private CommandManager commandManager;
    private Configuration configuration;
    private Messenger messenger;

    @Override
    public void onEnable() {
        this.commandManager = new SimpleCommandManager(this);
        this.configuration = new SimpleConfiguration(this);
        this.messenger = new SimpleMessenger(this);
        // TODO Loadouts
        // TODO Lobby
        // TODO Arenas
        // TODO Battles

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
    public Messenger getMessenger() {
        return messenger;
    }

    @Override
    public void reload() {
        configuration.reload();
        messenger.reload();
    }

}
