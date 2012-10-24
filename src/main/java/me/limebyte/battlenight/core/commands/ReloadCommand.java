package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Configuration;
import me.limebyte.battlenight.core.util.Messaging;
import me.limebyte.battlenight.core.util.Messaging.Message;

import org.bukkit.command.CommandSender;

public class ReloadCommand extends BattleNightCommand {

    protected ReloadCommand() {
        super("Reload");

        this.setLabel("reload");
        this.setDescription("Reloads BattleNight.");
        this.setUsage("/bn reload");
        this.setPermission(CommandPermission.ADMIN);
        this.setAliases(Arrays.asList("rl", "refresh", "restart"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messaging.tell(sender, Message.RELOADING);

        try {
            BattleNight.getBattle().stop();
            Configuration.reloadYamls();
            Messaging.tell(sender, Message.RELOAD_SUCCESSFUL);
            return true;
        } catch (Exception e) {
            Messaging.tell(sender, Message.RELOAD_FAILED);
            BattleNight.log.severe(e.getMessage());
            return false;
        }
    }

}
