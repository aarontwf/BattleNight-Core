package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BattleNightCommand {

    public ReloadCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public boolean onPerformed() {
        CommandSender sender = getSender();

        sender.sendMessage(BattleNight.BNTag + "Reloading config...");

        try {
            BattleNight.reloadConfigFiles();
            sender.sendMessage(BattleNight.BNTag + ChatColor.GREEN + "Reloaded successfully.");
            return true;
        } catch (Exception e) {
            sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "Reload failed.  See console for error log.");
            BattleNight.log.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public CommandPermission getPermission() {
        return CommandPermission.ADMIN;
    }

    @Override
    public String getUsage() {
        return "/bn reload";
    }

    @Override
    public String getConsoleUsage() {
        return "/bn reload";
    }

}
