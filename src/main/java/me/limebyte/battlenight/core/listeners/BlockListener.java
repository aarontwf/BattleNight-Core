package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends APIRelatedListener {

    public BlockListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(shouldPrevent(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        event.setCancelled(shouldPrevent(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(shouldPrevent(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(shouldPrevent(event.getPlayer()));
    }

    private boolean shouldPrevent(Player player) {
        Battle battle = getAPI().getBattle();
        if (player == null) return false;

        boolean inBattle = battle != null && battle.containsPlayer(player);
        return (inBattle && ConfigManager.get(Config.MAIN).getBoolean("BlockProtection", true));
    }
}
