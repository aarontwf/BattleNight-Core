package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.sound.Song;
import me.limebyte.battlenight.core.util.sound.SongLoader;

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
        // Commands with 1 argument
        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_TEST);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        if (args[0].equalsIgnoreCase("music")) {
            if (isNotPlayer(sender)) return false;

            SongLoader loader = new SongLoader(BattleNight.instance);
            Song song = loader.load("battle-end");
            song.play((Player) sender);
            return true;
        }

        // Commands with 2 arguments
        if (args.length < 2) {
            Messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        Messenger.tell(sender, Message.INVALID_COMMAND);
        return false;
    }

    private boolean isNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return true;
        }
        return false;
    }
}
