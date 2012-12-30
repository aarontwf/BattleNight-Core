package me.limebyte.battlenight.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.ClassManager;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;
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
        Messaging.tell(sender, Message.RELOADING);

        try {
            BattleNight.getBattle().stop();
            ConfigManager.reloadAll();
            ConfigManager.saveAll();
            ClassManager.reloadClasses();
            Messaging.tell(sender, Message.RELOAD_SUCCESSFUL);
            return true;
        } catch (Exception e) {
            Messaging.tell(sender, Message.RELOAD_FAILED);
            Messaging.log(Level.SEVERE, e.getStackTrace().toString());
            return false;
        }
    }

}
