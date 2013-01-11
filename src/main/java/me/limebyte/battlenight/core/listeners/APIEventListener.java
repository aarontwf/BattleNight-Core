package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.event.BattleRespawnEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class APIEventListener implements Listener {

    @EventHandler
    public void onBattleRespawn(BattleRespawnEvent event) {
        event.getBattle().onPlayerRespawn(event);
    }

}
