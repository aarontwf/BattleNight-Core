package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.TeamedBattle;
import me.limebyte.battlenight.api.event.BattleDeathEvent;

import org.bukkit.event.EventHandler;

public class ClassicBattle extends TeamedBattle {

    @Override
    public void onStart() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    @EventHandler
    public void onPlayerDeath(BattleDeathEvent event) {
        event.setCancelled(false);
    }
}
