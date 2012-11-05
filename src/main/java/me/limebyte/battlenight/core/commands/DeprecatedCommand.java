package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

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
        Messaging.tell(sender, Message.DEPRICATED_COMMAND, newLabel);
        return true;
    }

    public String getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

}
