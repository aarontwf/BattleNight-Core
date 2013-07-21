package me.limebyte.battlenight.core.tosort;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Teleporter implements Listener {

    public static Set<String> telePass = new HashSet<String>();
    private static Queue<String> playerQueue = new LinkedList<String>();
    private static Queue<Location> locationQueue = new LinkedList<Location>();
    private static int taskID = 0;

    public static void queue(Player player, Location location) {
        playerQueue.add(player.getName());
        locationQueue.add(location);
    }

    public static void queue(Player player, Waypoint waypoint) {
        queue(player, waypoint.getLocation());
    }

    public static void startTeleporting() {
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BattleNight.instance, new Runnable() {
            @Override
            public void run() {
                if (playerQueue.isEmpty()) {
                    stopTeleporting();
                } else {
                    tp(Bukkit.getPlayerExact(playerQueue.poll()), locationQueue.poll());
                }
            }
        }, 0L, 10L);
    }

    public static void tp(Player player, Location location) {
        if (player.hasMetadata("NPC")) return;
        String name = player.getName();

        telePass.add(name);
        player.teleport(location, TeleportCause.PLUGIN);
        telePass.remove(name);
    }

    public static void tp(Player player, Waypoint waypoint) {
        tp(player, waypoint.getLocation());
    }

    private static void stopTeleporting() {
        Bukkit.getServer().getScheduler().cancelTask(taskID);
        taskID = 0;
    }
}
