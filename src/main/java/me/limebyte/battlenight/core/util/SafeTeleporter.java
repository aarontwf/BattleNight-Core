package me.limebyte.battlenight.core.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class SafeTeleporter {

    private static Queue<String> playerQueue = new LinkedList<String>();
    private static Queue<Waypoint> waypointQueue = new LinkedList<Waypoint>();
    private static int taskID = 0;

    public static Map<String, Waypoint> locationQueue = new HashMap<String, Waypoint>();

    public static void queue(Player player, Waypoint waypoint) {
        playerQueue.add(player.getName());
        waypointQueue.add(waypoint);
    }

    public static void tp(Player player, Waypoint waypoint) {
        safeTP(player, waypoint);
    }

    public static void startTeleporting() {
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleNight.instance, new Runnable() {
            public void run() {
                if (playerQueue.isEmpty()) {
                    stopTeleporting();
                } else {
                    safeTP(Bukkit.getPlayerExact(playerQueue.poll()), waypointQueue.poll());
                }
            }
        }, 0L, 10L);
    }

    private static void stopTeleporting() {
        Bukkit.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
    }

    private static void safeTP(final Player player, Waypoint waypoint) {
        Location loc = waypoint.getLocation();
        String name = player.getName();

        BattleNight.BattleTelePass.put(name, "yes");
        player.teleport(loc, TeleportCause.PLUGIN);
        BattleNight.BattleTelePass.remove(name);

        locationQueue.put(name, waypoint);
    }
}
