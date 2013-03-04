package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.SpectatorManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpectatorListener extends APIRelatedListener {

    public SpectatorListener(BattleNightAPI api) {
        super(api);
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (getAPI().getBattle().containsPlayer(player)) {
            SpectatorManager manager = getAPI().getSpectatorManager();

            for (String spec : manager.getSpectators()) {
                Player spectator = Bukkit.getPlayerExact(spec);
                if (spectator == null) continue;

                if (manager.getTarget(spectator).getName() == name) {
                    spectator.teleport(player);
                }
            }
        }
    }
}
