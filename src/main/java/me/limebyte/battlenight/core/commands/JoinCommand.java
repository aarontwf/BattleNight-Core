package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand extends BattleNightCommand {

    protected JoinCommand() {
        super("Join");

        setLabel("join");
        setDescription("Join the Battle.");
        setUsage("/bn join");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("j", "play"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            api.getLobby().addPlayer((Player) sender);
            return true;
        } else {
            Messenger messenger = api.getMessenger();
            messenger.tell(sender, messenger.get("command.player-only"));
            return false;
        }
    }

}
