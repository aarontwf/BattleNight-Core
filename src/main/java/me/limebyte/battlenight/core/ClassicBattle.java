package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.TeamedBattle;
import me.limebyte.battlenight.api.event.BattleRespawnEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClassicBattle extends TeamedBattle implements Listener {

    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnd() {
        // TODO Auto-generated method stub

    }

    @EventHandler
    public void onBattleRespawn(BattleRespawnEvent event) {
        event.setCancelled(true);
    }
}
