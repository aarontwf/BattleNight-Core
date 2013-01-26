package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;

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
        Battle battle = api.getBattle();
        battle.removePlayer(player);
        battle.removeSpectator(player);
    }
}