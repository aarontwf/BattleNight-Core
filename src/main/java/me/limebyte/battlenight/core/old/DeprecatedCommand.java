package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.util.Message;

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

    public void setNewLabel(String newLabel) {
        this.newLabel = newLabel;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        api.getMessenger().tell(sender, Message.DEPRICATED_COMMAND, newLabel);
        return true;
    }

}
