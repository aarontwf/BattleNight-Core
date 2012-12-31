package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

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

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_TEST);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                BattleNight.instance.getAPI().getBattle().addPlayer((Player) sender);
                return true;
            } else {
                Messenger.tell(sender, Message.PLAYER_ONLY);
                return false;
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (sender instanceof Player) {
                BattleNight.instance.getAPI().getBattle().removePlayer((Player) sender);
                return true;
            } else {
                Messenger.tell(sender, Message.PLAYER_ONLY);
                return false;
            }
        } else return false;
    }

}
