package me.limebyte.battlenight.core.listeners;

import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener extends APIRelatedListener {

    public DeathListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Battle battle = getAPI().getBattle();
        Player player = event.getEntity();

        if (battle != null && battle.containsPlayer(player)) {
            getAPI().getMessenger().debug(Level.WARNING, "Somehow " + player.getName() + " died...");
        }
    }

}