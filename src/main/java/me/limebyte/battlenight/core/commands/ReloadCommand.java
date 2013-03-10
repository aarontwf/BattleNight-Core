package me.limebyte.battlenight.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.config.ConfigManager;

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
        Messenger.tell(sender, Message.RELOADING);

        try {
            api.getBattle().stop();

            ConfigManager.reloadAll();
            ConfigManager.saveAll();

            api.getArenaManager().saveArenas();
            api.getBattleManager().reloadBattles();
            api.getClassManager().reloadClasses();

            Messenger.tell(sender, Message.RELOAD_SUCCESSFUL);
            return true;
        } catch (Exception e) {
            Messenger.tell(sender, Message.RELOAD_FAILED);
            Messenger.log(Level.SEVERE, e.getStackTrace().toString());
            return false;
        }
    }

}
