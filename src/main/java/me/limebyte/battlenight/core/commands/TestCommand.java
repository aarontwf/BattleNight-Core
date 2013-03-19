package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.core.tosort.Messenger;
import me.limebyte.battlenight.core.tosort.Messenger.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends BattleNightCommand {

    protected TestCommand() {
        super("Test");

        setLabel("test");
        setDescription("Runs the specified test.");
        setUsage("/bn test <test>");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("try"));
    }

    @SuppressWarnings("unused")
    private boolean isNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        // Commands with 1 argument
        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_TEST);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        // Commands with 2 arguments
        if (args.length < 2) {
            Messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        Messenger.tell(sender, Message.INVALID_COMMAND);
        return false;
    }
}
