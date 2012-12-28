package me.limebyte.battlenight.core.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.chat.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.kitteh.tag.TagAPI;

public class SafeTeleporter implements Listener {

    private static Queue<String> playerQueue = new LinkedList<String>();
    private static Queue<Waypoint> waypointQueue = new LinkedList<Waypoint>();
    private static int taskID = 0;

    private static Map<String, Location> locationQueue = new HashMap<String, Location>();
    private static Set<Chunk> keepLoaded = new HashSet<Chunk>();

    public static void queue(Player player, Waypoint waypoint) {
        playerQueue.add(player.getName());
        waypointQueue.add(waypoint);
    }

    public static void tp(Player player, Waypoint waypoint) {
        tp(player, waypoint.getLocation());
    }

    public static void tp(Player player, Location location) {
        String name = player.getName();
        StackTraceElement[] element = new Exception().getStackTrace();
        Messaging.debug(Level.INFO, "Teleporting " + name + ".");
        Messaging.debug(Level.INFO, "Called by " + element[0].getClassName() + ":" + element[0].getLineNumber() + ".");
        Messaging.debug(Level.INFO, "Called by " + element[1].getClassName() + ":" + element[1].getLineNumber() + ".");
        Messaging.debug(Level.INFO, "Called by " + element[2].getClassName() + ":" + element[2].getLineNumber() + ".");

        //safeTP(player, waypoint);
        BattleNight.BattleTelePass.put(name, "yes");
        player.teleport(location, TeleportCause.PLUGIN);
        BattleNight.BattleTelePass.remove(name);
    }

    public static void startTeleporting() {
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleNight.getInstance(), new Runnable() {
            public void run() {
                if (playerQueue.isEmpty()) {
                    stopTeleporting();
                } else {
                    tp(Bukkit.getPlayerExact(playerQueue.poll()), waypointQueue.poll());
                }
            }
        }, 0L, 10L);
    }

    private static void stopTeleporting() {
        Bukkit.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
    }

    @SuppressWarnings("unused")
    private static void safeTP(final Player player, Waypoint waypoint) {
        Location loc = waypoint.getLocation();
        loc.setY(loc.getY() + 0.5);

        Chunk chunk = loc.getChunk();
        keepLoaded.add(chunk);
        chunk.load();

        String name = player.getName();

        BattleNight.BattleTelePass.put(name, "yes");
        player.teleport(loc, TeleportCause.PLUGIN);
        BattleNight.BattleTelePass.remove(name);

        locationQueue.put(name, loc);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        if (locationQueue.containsKey(name)) {
            Location loc = locationQueue.get(name);
            locationQueue.remove(name);

            BattleNight.BattleTelePass.put(name, "yes");
            player.teleport(loc, TeleportCause.PLUGIN);
            BattleNight.BattleTelePass.remove(name);

            try {
                TagAPI.refreshPlayer(player);
            } catch (Exception e) {
            }
        } else {
            Chunk chunk = event.getTo().getChunk();

            if (keepLoaded.contains(chunk)) {
                keepLoaded.remove(chunk);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (keepLoaded.contains(event.getChunk())) {
            event.setCancelled(true);
        }
    }
}
