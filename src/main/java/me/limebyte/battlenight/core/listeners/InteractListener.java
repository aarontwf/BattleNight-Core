package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;
import me.limebyte.battlenight.core.util.Util;
import me.limebyte.battlenight.core.util.player.BattlePlayer;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener extends APIRelatedListener {

    public InteractListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        BattlePlayer bPlayer = BattlePlayer.get(player.getUniqueId());
        if (!bPlayer.isAlive()) event.setCancelled(true);

        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if (block.getType() == Util.getMaterial(ConfigManager.get(Config.MAIN).getString("ReadyBlock", "iron-block"))) {
                bPlayer.setReady(true);
            }
        }
    }
}