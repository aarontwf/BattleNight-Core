package me.limebyte.battlenight.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.tosort.ConfigManager;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends BattleNightCommand {

    protected ReloadCommand() {
        super("Reload");

        setLabel("reload");
        setDescription("Reloads BattleNight.");
        setUsage("/bn reload");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("rl", "refresh", "restart"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();
        messenger.tell(sender, Message.RELOADING);

        try {
            api.getBattleManager().getBattle().stop();

            ConfigManager.reloadAll();
            ConfigManager.saveAll();

            api.getArenaManager().saveArenas();
            api.getClassManager().reloadClasses();

            messenger.tell(sender, Message.RELOAD_SUCCESSFUL);
            return true;
        } catch (Exception e) {
            messenger.tell(sender, Message.RELOAD_FAILED);
            messenger.log(Level.SEVERE, e.getStackTrace().toString());
            return false;
        }
    }

}
