package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends BattleNightCommand {

    protected TestCommand() {
        super("Test");

        this.setLabel("test");
        this.setDescription("Runs the specified test.");
        this.setUsage("/bn test <test>");
        this.setPermission(CommandPermission.ADMIN);
        this.setAliases(Arrays.asList("try"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            Messaging.tell(sender, Message.SPECIFY_TEST);
            Messaging.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (sender instanceof Player) {
                BattleNight.getNewBattle().addPlayer((Player) sender);
                return true;
            } else {
                Messaging.tell(sender, Message.PLAYER_ONLY);
                return false;
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (sender instanceof Player) {
                BattleNight.getNewBattle().removePlayer((Player) sender);
                return true;
            } else {
                Messaging.tell(sender, Message.PLAYER_ONLY);
                return false;
            }
        } else {
            return false;
        }
    }

}
