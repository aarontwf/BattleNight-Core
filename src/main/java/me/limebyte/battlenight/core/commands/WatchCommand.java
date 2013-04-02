package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WatchCommand extends BattleNightCommand {

    protected WatchCommand() {
        super("Watch");

        setLabel("watch");
        setDescription("Watch the Battle.");
        setUsage("/bn watch");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("w", "spectate", "view"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Battle battle = api.getBattle();

            SpectatorManager spectatorManager = api.getSpectatorManager();

            if (!battle.isInProgress()) {
                messenger.tell(sender, Message.BATTLE_NOT_IN_PROGRESS);
                return false;
            }

            if (spectatorManager.getSpectators().contains(player.getName())) {
                messenger.tell(sender, Message.ALREADY_SPECTATING);
                return false;
            }

            if (battle.containsPlayer(player)) {
                messenger.tell(sender, Message.CANT_SPECTATE);
                return false;
            }

            spectatorManager.addSpectator(player, true);
            return true;
        } else {
            messenger.tell(sender, Message.PLAYER_ONLY);
            return false;
        }
    }

}
