package org.battlenight.core.message;

import java.util.UUID;
import java.util.logging.Logger;

import org.battlenight.api.configuration.ConfigFile;
import org.battlenight.api.game.Lobby;
import org.battlenight.api.message.Messenger;
import org.battlenight.core.BattleNight;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SimpleMessenger implements Messenger {

    private static final String PREFIX = ChatColor.GRAY + "[BattleNight] " + ChatColor.RESET;

    private BattleNight plugin;
    private Logger log;
    private boolean debugging;
    private FileConfiguration messages;
    private FileConfiguration customMessages;

    public SimpleMessenger(BattleNight plugin) {
        this.plugin = plugin;
        reload();
    }

    @Override
    public void debug(String path, Object... args) {
        if (debugging) {
            String message = getMessage(path, args);
            if (!message.isEmpty()) log.info("[Debug] " + message);
        }
    }

    @Override
    public String format(String message, Object... args) {
        for (int i = 0; i < args.length; i++) {
            message = message.replace("$" + (i + 1), describeObject(args[i]));
        }
        return message;
    }

    @Override
    public void logError(String path, Object... args) {
        String message = getMessage(path, args);
        if (!message.isEmpty()) log.severe(message);
    }

    @Override
    public void logInfo(String path, Object... args) {
        String message = getMessage(path, args);
        if (!message.isEmpty()) log.info(message);
    }

    @Override
    public void logWarning(String path, Object... args) {
        String message = getMessage(path, args);
        if (!message.isEmpty()) log.warning(message);
    }

    @Override
    public void reload() {
        log = plugin.getLogger();

        FileConfiguration options = plugin.getConfiguration().get(ConfigFile.OPTIONS);
        debugging = options.getBoolean("debugging");

        ConfigFile messageFile = ConfigFile.getByName(options.getString("language"));
        messages = plugin.getConfiguration().get(messageFile != null ? messageFile : ConfigFile.MSG_ENGLISH);
        customMessages = plugin.getConfiguration().get(ConfigFile.MSG_CUSTOM);
    }

    @Override
    public void send(CommandSender sender, String path, Object... args) {
        String message = getMessage(path, args);
        if (!message.isEmpty()) sender.sendMessage(PREFIX + message);
    }

    @Override
    public void sendLobby(String message, Object... args) {
        Lobby lobby = plugin.getLobby();
        for (UUID uuid : lobby.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) send(player, message, args);
        }
    }

    private String describeObject(Object object) {
        if (object instanceof String) return (String) object;
        else if (object instanceof Player) return ((Player) object).getDisplayName();
        return object.toString();
    }

    private String getMessage(String path, Object... args) {
        String message = customMessages.getString(path, messages.getString(path, path));
        return format(ChatColor.translateAlternateColorCodes('&', message), args);
    }

}
