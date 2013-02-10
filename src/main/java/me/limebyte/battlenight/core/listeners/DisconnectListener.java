package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleDeathEvent;
import me.limebyte.battlenight.api.util.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {

    private BattleNightAPI api;

    public DisconnectListener(BattleNightAPI api) {
        this.api = api;
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
            battle = api.getBattle();
            battle.removePlayer(player);
            battle.removeSpectator(player);
        }
    }
}