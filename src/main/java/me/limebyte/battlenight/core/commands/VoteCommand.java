package me.limebyte.battlenight.core.commands;

import org.bukkit.command.CommandSender;

public class VoteCommand extends BattleNightCommand {

    public VoteCommand() {
        super("Vote");

        setLabel("vote");
        setDescription("Vote for an arena.");
        setUsage("/bn vote [arena]");
        setPermission(CommandPermission.USER);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }
        return false;
    }

}
