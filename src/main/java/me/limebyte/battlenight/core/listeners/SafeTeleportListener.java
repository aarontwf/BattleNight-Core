package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.SafeTeleporter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.kitteh.tag.TagAPI;

public class SafeTeleportListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (SafeTeleporter.locationQueue.containsKey(name)) {
            SafeTeleporter.locationQueue.remove(name);

            BattleNight.BattleTelePass.put(name, "yes");
            player.teleport(event.getTo(), TeleportCause.PLUGIN);
            BattleNight.BattleTelePass.remove(name);

            try {
                TagAPI.refreshPlayer(player);
            } catch (Exception e) {
            }
        }
    }

}
