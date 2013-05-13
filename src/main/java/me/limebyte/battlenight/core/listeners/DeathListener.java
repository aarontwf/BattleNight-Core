package me.limebyte.battlenight.core.listeners;

import java.util.HashSet;
import java.util.Set;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.battle.SimpleBattle;
import me.limebyte.battlenight.core.tosort.PlayerData;
import me.limebyte.battlenight.core.util.PlayerStats;

import org.bukkit.Bukkit;
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
        String name = player.getName();
        Battle battle = getAPI().getBattleManager().getBattle();

        if (battle.containsPlayer(player)) {
            event.getDrops().clear();
            event.setDroppedExp(0);

            if (battle.isInProgress()) {
                killFeed(player, player.getKiller(), event.getDeathMessage());
                event.setDeathMessage("");
            }

            Player killer = player.getKiller();
            PlayerStats stats = PlayerStats.get(name);
            boolean suicide = true;
            
            if (killer != null && killer != player) {
                stats.addKill(false);
                battle.addKill(killer);
                suicide = false;
            }
            
            stats.addDeath(suicide);
            
            // Update leading
            updateLeaders((SimpleBattle) battle, stats);
            
            // Old Stuff
            battle.addDeath(player);

            queue.add(name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        SpectatorManager spectatorManager = getAPI().getSpectatorManager();

        if (queue.contains(name)) {
            queue.remove(name);
            event.setRespawnLocation(getAPI().getBattleManager().getBattle().respawn(player));
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

        messenger.tellBattle(deathMessage);
    }
    
    private void updateLeaders(SimpleBattle battle, PlayerStats stats) {
        int leadingScore = 0;
        Set<String> leaders = (battle).leadingPlayers;
        Player leader = Bukkit.getPlayerExact(leaders.iterator().next());
        
        if (leader != null) {
            leadingScore = PlayerStats.get(leader.getName()).getScore();
        }
        
        if (leadingScore > stats.getScore()) return;
        if (leadingScore < stats.getScore()) leaders.clear();
        leaders.add(leader.getName());
    }

}