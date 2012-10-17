package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DeprecatedCommand extends BattleNightCommand {

    String newLabel;

    protected DeprecatedCommand(String label, String newLabel) {
        super("Deprecated");

        this.setLabel(label);
        this.setDescription("Deprecated Command.");

        this.setNewLabel(newLabel);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        sender.sendMessage(BattleNight.BNTag + ChatColor.RED + "This command is deprecated, please use \"/bn " + newLabel + "\" instead.");
        return true;
    }

    public String getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

}
