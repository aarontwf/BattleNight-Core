package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.util.Message;

import org.bukkit.command.CommandSender;

public class StartCommand extends BattleNightCommand {

    public StartCommand() {
        super("Start");

        setLabel("start");
        setDescription("Force starts the Battle.");
        setUsage("/bn start");
        setPermission(CommandPermission.MODERATOR);
        setAliases(Arrays.asList("begin"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        api.getLobby().startBattle();
        api.getMessenger().tell(sender, Message.BATTLE_STARTED);
        return false;
    }

}
