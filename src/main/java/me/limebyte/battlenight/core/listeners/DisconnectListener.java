package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.tosort.BattleDeathEvent;
import me.limebyte.battlenight.api.tosort.PlayerData;
import me.limebyte.battlenight.core.Battle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener extends APIRelatedListener {

    public DisconnectListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        removePlayer(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event) {
        removePlayer(event);
    }

    private void removePlayer(PlayerEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Battle battle;

        if (DeathListener.queue.containsKey(name)) {
            BattleDeathEvent apiEvent = DeathListener.queue.get(name);
            DeathListener.queue.remove(name);
            battle = apiEvent.getBattle();

            if (apiEvent.isCancelled()) {
                battle.respawn(player);
                battle.removePlayer(player);
            } else {
                PlayerData.reset(player);
                PlayerData.restore(player, true, false);
            }
        } else {
            battle = getAPI().getBattle();
            battle.removePlayer(player);
            getAPI().getSpectatorManager().removeSpectator(player);
        }
    }
}