package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleDeathEvent;
import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FFABattle extends Battle {

    private static final int LIVES = 5;

    @Override
    public void onStart() {
        for (String name : getPlayers()) {
            Player player = Bukkit.getPlayerExact(name);
            if (player == null) continue;
            Metadata.set(player, "lives", LIVES);
        }
    }

    @Override
    public void onEnd() {
        for (String name : getPlayers()) {
            Player player = Bukkit.getPlayerExact(name);
            if (player == null) continue;
            Metadata.set(player, "lives", 0);
        }
    }

    @Override
    public void onPlayerDeath(BattleDeathEvent event) {
        int lives = Metadata.getInt(event.getPlayer(), "lives");
        Metadata.set(event.getPlayer(), "lives", --lives);
        if (lives > 0) event.setCancelled(true);
    }

}
