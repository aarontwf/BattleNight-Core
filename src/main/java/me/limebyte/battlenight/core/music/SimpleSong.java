package me.limebyte.battlenight.core.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Song;
import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SimpleSong implements Song {

    private ArrayList<Note> notes;
    private long length;
    private HashMap<UUID, Integer> listeners;

    public SimpleSong(BattleNightAPI api, long length) {
        notes = new ArrayList<Note>();
        listeners = new HashMap<UUID, Integer>();
        this.length = length;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public boolean isListening(Player player) {
        return listeners.containsKey(player.getUniqueId());
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public void play(Player player) {
        stop(player);
        BukkitTask task = new MusicPlayer(player).runTaskTimer(BattleNight.instance, 0L, 2L);
        listeners.put(player.getUniqueId(), task.getTaskId());
    }

    @Override
    public void stop(Player player) {
        if (isListening(player)) {
            UUID id = player.getUniqueId();
            Bukkit.getServer().getScheduler().cancelTask(listeners.get(id));
            listeners.remove(id);
        }
    }

    class MusicPlayer extends BukkitRunnable {
        Player player;
        long tick = 0L;

        public MusicPlayer(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            if (tick > length * 2L) {
                stop(player);
                return;
            }
            if (!player.isOnline()) {
                stop(player);
                return;
            }
            for (Note note : notes) {
                if (tick == note.getTick()) {
                    note.play(player);
                }
            }
            tick += 2L;
        }

    }
}
