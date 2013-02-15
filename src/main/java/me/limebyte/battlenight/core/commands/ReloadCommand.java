package me.limebyte.battlenight.core.commands;

import java.util.Arrays;
import java.util.logging.Level;

import me.limebyte.battlenight.api.managers.BattleManager;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.managers.ClassManager;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

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
            ClassManager.reloadClasses();
            api.getArenaManager().saveArenas();

            // Battle setting
            BattleManager battleManager = api.getBattleManager();
            String battle = ConfigManager.get(Config.MAIN).getString("BattleType", "TDM");
            if (battleManager.getBattle(battle) == null) battle = "TDM";
            battleManager.setActiveBattle(battle);

            Messenger.tell(sender, Message.RELOAD_SUCCESSFUL);
            return true;
        } catch (Exception e) {
            Messenger.tell(sender, Message.RELOAD_FAILED);
            Messenger.log(Level.SEVERE, e.getStackTrace().toString());
            return false;
        }
    }

}
