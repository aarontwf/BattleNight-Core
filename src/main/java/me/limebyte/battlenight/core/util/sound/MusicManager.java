package me.limebyte.battlenight.core.util.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public class MusicManager {
    private Plugin plugin;

    private File file;
    private File customFile;

    public MusicManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().toString() + "/music/");
        this.customFile = new File(plugin.getDataFolder().toString() + "/music/custom/");

        if (!this.file.exists()) this.file.mkdirs();
        if (!this.customFile.exists()) this.customFile.mkdirs();
    }

    public void loadSongs() {
        Song.battleEnd = load("battle-end");
    }

    public Song load(String name) {
        File file = new File(this.file + "/" + name + ".nbs");
        if (!file.exists()) {
            try {
                loadDefault(name, file);
            } catch (IOException e) {
                Messenger.log(Level.WARNING, "Failed to load song \"" + name + "\"from the jar.");
            }
        }

        File customFile = new File(this.customFile + "/" + name + ".nbs");
        if (!customFile.exists()) file = new File(this.file + "/" + name + ".nbs");

        if (customFile.exists()) {
            Messenger.debug(Level.INFO, "Using custom " + name + " music.");
            return getSong(customFile);
        }

        return getSong(file);
    }

    private boolean loadDefault(String name, File file) throws IOException {
        InputStream inputStream = plugin.getResource("music/" + name + ".nbs");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] arrayOfByte = new byte[10240];
        int k = inputStream.read(arrayOfByte);
        while (k != -1) {
            outputStream.write(arrayOfByte, 0, k);
            k = inputStream.read(arrayOfByte);
        }

        outputStream.close();
        inputStream.close();
        return true;
    }

    @SuppressWarnings("unused")
    private Song getSong(File file) {
        UtilDataInput data = null;
        try {
            data = new UtilDataInput(new FileInputStream(file));

            int songLength = data.readShort();
            int songHeight = data.readShort();

            String songName = data.readString();
            String songAuthor = data.readString();
            String originalSongAuthor = data.readString();
            String songDescription = data.readString();

            int tempo = data.readShort();
            if ((tempo != 1000) && (tempo != 500) && (tempo != 250)) return null;

            // Auto-saving, Auto-saving duration, Time signature
            data.skipBytes(3);

            int minutesSpent = data.readInt();
            int leftClicks = data.readInt();
            int rightClicks = data.readInt();
            int blocksAdded = data.readInt();
            int blocksRemoved = data.readInt();
            String exportName = data.readString();

            Song song = new Song(songLength);

            int ticks = -1;
            int jumps = 0;

            while (true) {
                jumps = data.readShort();
                if (jumps == 0) break;
                ticks += jumps;

                short layer = -1;
                while (true) {
                    jumps = data.readShort();
                    if (jumps == 0) break;
                    layer += jumps;
                    byte inst = data.readByte();
                    Sound sound = getSound(inst);
                    byte pitch = (byte) (data.readByte() - 33);

                    if (sound == null || (pitch < 0 || pitch > 24)) continue;
                    song.addNote(new Note(sound, ticks * getTicks(tempo), 1.0f, pitch));
                }
            }

            data.closeStream();
            return song;
        } catch (Exception e) {
            return null;
        }
    }

    private int getTicks(int tempo) {
        switch (tempo) {
            case 1000:
                return 2;
            case 500:
                return 4;
            case 250:
                return 8;
        }
        return 0;
    }

    private Sound getSound(byte inst) {
        try {
            switch (inst) {
                case 0:
                    return Sound.NOTE_PIANO;
                case 1:
                    return Sound.NOTE_BASS_GUITAR;
                case 2:
                    return Sound.NOTE_BASS_DRUM;
                case 3:
                    return Sound.NOTE_SNARE_DRUM;
                case 4:
                    return Sound.NOTE_STICKS;
            }
            return null;
        } catch (Exception e) {
        }
        return null;
    }

}
