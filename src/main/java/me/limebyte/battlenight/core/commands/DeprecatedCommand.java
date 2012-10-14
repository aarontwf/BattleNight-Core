package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DeprecatedCommand extends BNCommand {

    String newCommand;

    public DeprecatedCommand(CommandSender sender, String[] args, String newCommand) {
        super(sender, args);
        this.newCommand = newCommand;
    }

    @Override
    public boolean onPerformed() {
        getSender().sendMessage(BattleNight.BNTag + ChatColor.RED + "This command is deprecated, please use \"" + newCommand + "\" instead.");
        return true;
    }

    @Override
    public CommandPermission getPermission() {
        return CommandPermission.USER;
    }

    @Override
    public String getUsage() {
        return "/bn [command]";
    }

    @Override
    public String getConsoleUsage() {
        return "/bn [command]";
    }

}
