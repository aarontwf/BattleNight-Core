package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.command.CommandSender;

public class DeprecatedCommand extends BattleNightCommand {

    String newLabel;

    protected DeprecatedCommand(String label, String newLabel) {
        super("Deprecated");

        setLabel(label);
        setDescription("Deprecated Command.");

        setNewLabel(newLabel);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger.tell(sender, Message.DEPRICATED_COMMAND, newLabel);
        return true;
    }

    public String getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

}
