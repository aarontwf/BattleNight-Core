package me.limebyte.battlenight.core.listeners;

import java.util.HashMap;
import java.util.Map;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.event.BattleDeathEvent;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener extends APIRelatedListener {

    protected static Map<String, BattleDeathEvent> queue = new HashMap<String, BattleDeathEvent>();

    public DeathListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Battle battle = getAPI().getBattle();

        if (battle.containsPlayer(player)) {
            event.getDrops().clear();

            if (battle.isInProgress()) {
                Messenger.killFeed(player, player.getKiller(), event.getDeathMessage());
                event.setDeathMessage("");
            }

            BattleDeathEvent apiEvent = new BattleDeathEvent(battle, player);
            Bukkit.getServer().getPluginManager().callEvent(apiEvent);

            if (!apiEvent.isCancelled()) {
                apiEvent.setRespawnLocation(battle.toSpectator(player, true));
            }

            queue.put(player.getName(), apiEvent);

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        Battle battle = getAPI().getBattle();

        if (queue.containsKey(name)) {
            BattleDeathEvent apiEvent = queue.get(name);
            queue.remove(name);

            if (apiEvent.isCancelled()) {
                apiEvent.getBattle().respawn(player);
            } else if (!apiEvent.isCancelled() && !apiEvent.getBattle().containsSpectator(player)) {
                PlayerData.restore(player, true, false);
            }

            event.setRespawnLocation(apiEvent.getRespawnLocation());
        } else if (battle.containsSpectator(player)) {
            event.setRespawnLocation(PlayerData.getSavedLocation(player));
            battle.removeSpectator(player);
        }
    }

}