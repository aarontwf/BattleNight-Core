package me.limebyte.battlenight.core.listeners;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.tosort.PlayerData;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener extends APIRelatedListener {

    protected static Set<String> queue = new HashSet<String>();

    public DeathListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Battle battle = getAPI().getBattle();

        if (battle.containsPlayer(player)) {
            event.getDrops().clear();
            event.setDroppedExp(0);

            if (battle.isInProgress()) {
                killFeed(player, player.getKiller(), event.getDeathMessage());
                event.setDeathMessage("");
            }

            Player killer = player.getKiller();

            battle.addDeath(player);
            if (killer != null && killer != player) {
                battle.addKill(killer);
            }

            queue.add(player.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SpectatorManager spectatorManager = getAPI().getSpectatorManager();

        if (queue.contains(name)) {
            queue.remove(name);
            event.setRespawnLocation(getAPI().getBattle().respawn(player));
        } else if (spectatorManager.getSpectators().contains(player.getName())) {
            event.setRespawnLocation(PlayerData.getSavedLocation(player));
            spectatorManager.removeSpectator(player);
        }
    }

    private void killFeed(Player player, Player killer, String deathMessage) {
        Messenger messenger = getAPI().getMessenger();

        deathMessage = ChatColor.GRAY + deathMessage;
        deathMessage = deathMessage.replaceAll(player.getName(), messenger.getColouredName(player) + ChatColor.GRAY);
        deathMessage = deathMessage.replaceAll(player.getDisplayName(), messenger.getColouredName(player) + ChatColor.GRAY);

        if (killer != null) {
            deathMessage = deathMessage.replaceAll(killer.getName(), messenger.getColouredName(killer) + ChatColor.GRAY);
            deathMessage = deathMessage.replaceAll(killer.getDisplayName(), messenger.getColouredName(killer) + ChatColor.GRAY);
        }

        messenger.tellEveryone(deathMessage);
    }

}