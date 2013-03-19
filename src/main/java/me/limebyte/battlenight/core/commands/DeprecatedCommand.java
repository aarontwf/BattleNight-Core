package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.core.tosort.Messenger;
import me.limebyte.battlenight.core.tosort.Messenger.Message;

import org.bukkit.command.CommandSender;

public class DeprecatedCommand extends BattleNightCommand {

    String newLabel;

    protected DeprecatedCommand(String label, String newLabel) {
        super("Deprecated");

        setLabel(label);
        setDescription("Deprecated Command.");

        setNewLabel(newLabel);
    }

    public String getNewLabel() {
        return newLabel;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger.tell(sender, Message.DEPRICATED_COMMAND, newLabel);
        return true;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

}
