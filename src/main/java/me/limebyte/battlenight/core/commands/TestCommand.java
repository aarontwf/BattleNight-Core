package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.commands.CommandPermission;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.util.SimpleMessenger.Message;

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
            api.getMessenger().tell(sender, Message.PLAYER_ONLY);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        // Commands with 1 argument
        if (args.length < 1) {
            messenger.tell(sender, Message.SPECIFY_TEST);
            messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        // Commands with 2 arguments
        if (args.length < 2) {
            messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        messenger.tell(sender, Message.INVALID_COMMAND);
        return false;
    }
}
