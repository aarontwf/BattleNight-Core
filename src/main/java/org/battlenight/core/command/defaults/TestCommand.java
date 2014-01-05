package org.battlenight.core.command.defaults;

import java.util.Arrays;
import java.util.List;

import org.battlenight.api.command.BattleNightCommand;
import org.battlenight.api.message.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class TestCommand extends BattleNightCommand {

    private static final List<String> PRIMARY_OPTIONS = ImmutableList.of("msg", "join");

    public TestCommand() {
        super("Test");
    }

    @Override
    public boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = getApi().getMessenger();

        if (args.length < 1) {
            messenger.send(sender, "command.general.specify", "test");
            return false;
        }

        if (!PRIMARY_OPTIONS.contains(args[0].toLowerCase())) {
            messenger.send(sender, "command.general.non-existent", "test");
            return false;
        }

        if (args[0].equalsIgnoreCase("msg")) {
            if (args.length < 2) {
                messenger.send(sender, "command.general.specify", "message");
                return false;
            }

            messenger.send(sender, args[1], (Object[]) Arrays.copyOfRange(args, 2, args.length));
        } else if (args[0].equalsIgnoreCase("join")) {
            if (!(sender instanceof Player)) {
                messenger.send(sender, "command.general.player-only");
                return true;
            }

            getApi().getLobby().addPlayer((Player) sender);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 0) return PRIMARY_OPTIONS;
        return ImmutableList.of();
    }

}
