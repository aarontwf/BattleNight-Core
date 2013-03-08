package me.limebyte.battlenight.core.util.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Song {
    private ArrayList<Note> notes;
    private long length;
    private HashMap<String, Integer> listeners;

    public static Song battleEnd;

    public Song(long length) {
        notes = new ArrayList<Note>();
        listeners = new HashMap<String, Integer>();
        this.length = length;
    }

    public void addNote(Note note) {
        this.notes.add(note);
        Messenger.debug(Level.INFO, "Added " + note.toString());
    }

    public void play(Player player) {
        stop(player);
        BukkitTask task = new MusicPlayer(player).runTaskTimer(BattleNight.instance, 0L, 2L);
        listeners.put(player.getName(), task.getTaskId());
    }

    public void stop(Player player) {
        if (isListening(player)) {
            String name = player.getName();
            Bukkit.getServer().getScheduler().cancelTask(listeners.get(name));
            listeners.remove(name);
        }
    }

    public boolean isListening(Player player) {
        return listeners.containsKey(player.getName());
    }

    public long length() {
        return length;
    }

    public long getLengthInSeconds() {
        return length / 10L;
    }

    class MusicPlayer extends BukkitRunnable {
        Player player;
        long tick = 0L;

        public MusicPlayer(Player player) {
            this.player = player;
        }

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
                if (tick == note.getTick()) note.play(player);
            }
            tick += 2L;
        }

    }
}
