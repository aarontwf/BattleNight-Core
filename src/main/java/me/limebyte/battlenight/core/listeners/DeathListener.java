package me.limebyte.battlenight.core.listeners;

import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.core.tosort.PlayerData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener extends APIRelatedListener {
    
    public DeathListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Battle battle = getAPI().getBattleManager().getBattle();
        Player player = event.getEntity();

        if (battle.containsPlayer(player)) {
            getAPI().getMessenger().debug(Level.WARNING, "Somehow " + player.getName() + " died...");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        SpectatorManager spectatorManager = getAPI().getSpectatorManager();

        if (spectatorManager.getSpectators().contains(player.getName())) {
            event.setRespawnLocation(PlayerData.getSavedLocation(player));
            spectatorManager.removeSpectator(player);
        }
    }

}