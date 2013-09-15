package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.core.util.UpdateChecker;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener extends APIRelatedListener {

    public ConnectionListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (UpdateChecker.isUpdateAvailable() && player.isOp()) {
            player.sendMessage(ChatColor.AQUA + UpdateChecker.getUpdateMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        removePlayer(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event);
    }

    private void removePlayer(PlayerEvent event) {
        Player player = event.getPlayer();
        Lobby lobby = getAPI().getLobby();
        Battle battle = getAPI().getBattle();

        if (lobby.contains(player)) {
            lobby.removePlayer(player);
        }
        if (battle != null) {
            battle.removePlayer(player);
        }
    }
}