package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

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
            Messenger messenger = api.getMessenger();
            messenger.tell(sender, messenger.get("command.player-only"));
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
            messenger.tell(sender, messenger.get("command.usage"), getUsage());
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
