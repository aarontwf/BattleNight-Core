package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.commands.CommandPermission;
import me.limebyte.battlenight.core.util.SimpleMessenger.Message;

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
        if (sender instanceof Player)
            return api.getBattle().addPlayer((Player) sender);
        else {
            api.getMessenger().tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
