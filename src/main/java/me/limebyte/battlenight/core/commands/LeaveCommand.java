package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand extends BattleNightCommand {

    protected LeaveCommand() {
        super("Leave");

        setLabel("leave");
        setDescription("Leave the Battle.");
        setUsage("/bn leave");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("l", "quit"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        if (sender instanceof Player) {
            Battle battle = api.getBattle();
            Lobby lobby = api.getLobby();
            Player player = (Player) sender;

            if (battle != null && battle.containsPlayer(player)) return battle.removePlayer(player);
            else if (lobby.getPlayers().contains(player.getUniqueId())) {
                lobby.removePlayer(player);
                return true;
            } else {
                messenger.tell(sender, messenger.get("battle.not-in"));
                return false;
            }
        } else {
            messenger.tell(sender, messenger.get("command.player-only"));
            return false;
        }
    }

}
