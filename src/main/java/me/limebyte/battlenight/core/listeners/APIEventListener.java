package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.tosort.BattleDeathEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class APIEventListener implements Listener {

    @EventHandler
    public void onBattleRespawn(BattleDeathEvent event) {
        event.getBattle().onPlayerDeath(event);
    }

}
