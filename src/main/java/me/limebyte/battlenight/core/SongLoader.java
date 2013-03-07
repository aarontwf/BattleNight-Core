package me.limebyte.battlenight.core;

import java.io.File;
import java.io.FileInputStream;

import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public class SongLoader {
    private Plugin plugin;
    private File file;

    public SongLoader(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().toString() + "/music/");
        if (!this.file.exists()) {
            this.file.mkdirs();
        }
    }

    public Song load(String name) {
        File localFile = new File(this.file + "/" + name + ".nbs");
        if (!localFile.exists()) return null;

        return parseSong(localFile);
    }

    @SuppressWarnings("unused")
    private Song parseSong(File file) {
        UtilDataInput data = null;
        Song song = new Song();
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
                    byte key = (byte) (data.readByte() - 33);
                    Sound sound = getSound(inst);

                    if (sound == null || (key < 0 || key > 24)) continue;
                    song.addNote(new Note(sound, key, ticks * getTicks(tempo)));
                }
            }

            data.closeStream();
            return song;
        } catch (Exception e) {
            d("ERROR: Couldn't load the specified melody file.");
            return null;
        }
    }

    private int getTicks(int paramInt) {
        switch (paramInt) {
            case 1000:
                return 2;
            case 500:
                return 4;
            case 250:
                return 8;
        }
        return 0;
    }

    private Sound getSound(byte paramByte) {
        try {
            switch (paramByte) {
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
        } catch (Exception localException) {
        }
        return null;
    }

    private void d(String paramString) {
        System.out.println("[" + this.plugin.getName() + "] " + paramString);
    }

}
