package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.util.player.BattlePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommand extends BattleNightCommand {

    protected ReadyCommand() {
        super("Ready");

        setLabel("ready");
        setDescription("Ready up.");
        setUsage("/bn ready");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("readyup"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();
        Lobby lobby = api.getLobby();

        if (!(sender instanceof Player)) {
            messenger.tell(sender, messenger.get("command.player-only"));
            return false;
        }

        Player player = (Player) sender;

        if (!lobby.contains(player)) {
            messenger.tell(sender, messenger.get("lobby.not-in"));
            return false;
        }

        BattlePlayer.get(player.getName()).setReady(true);
        return true;
    }

}
