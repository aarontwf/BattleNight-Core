package me.limebyte.battlenight.core.util;

import java.util.ArrayList;
import java.util.HashMap;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Song;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.tosort.Note;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SimpleSong implements Song {

    private ArrayList<Note> notes;
    private long length;
    private HashMap<String, Integer> listeners;

    public SimpleSong(BattleNightAPI api, long length) {
        notes = new ArrayList<Note>();
        listeners = new HashMap<String, Integer>();
        this.length = length;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    @Override
    public boolean isListening(Player player) {
        return listeners.containsKey(player.getName());
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public void play(Player player) {
        stop(player);
        BukkitTask task = new MusicPlayer(player).runTaskTimer(BattleNight.instance, 0L, 2L);
        listeners.put(player.getName(), task.getTaskId());
    }

    @Override
    public void stop(Player player) {
        if (isListening(player)) {
            String name = player.getName();
            Bukkit.getServer().getScheduler().cancelTask(listeners.get(name));
            listeners.remove(name);
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
