package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.ChatColor;
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
        sender.sendMessage(BattleNight.BNTag + "Reloading BattleNight...");

        try {
            BattleNight.getBattle().stop();
            BattleNight.reloadConfigFiles();
            sender.sendMessage(BattleNight.BNTag + ChatColor.GREEN + "Reloaded successfully.");
            return true;
        } catch (Exception e) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Reload failed.  See console for error log.");
            BattleNight.log.severe(e.getMessage());
            return false;
        }
    }

}
