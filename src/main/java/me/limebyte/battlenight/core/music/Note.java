package me.limebyte.battlenight.core.music;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Note {
    private final Sound sound;

    private final long tick;
    private final float pitch;
    private final float volume;

    public Note(Sound sound, long tick, float volume, int pitch) {
        this.sound = sound;
        this.tick = tick;
        this.volume = 1.0f;
        this.pitch = convertPitch(pitch);
    }

    public static float convertPitch(int pitch) {
        switch (pitch) {
            case 0:
                return 0.5F;
            case 1:
                return 0.53F;
            case 2:
                return 0.56F;
            case 3:
                return 0.6F;
            case 4:
                return 0.63F;
            case 5:
                return 0.67F;
            case 6:
                return 0.7F;
            case 7:
                return 0.76F;
            case 8:
                return 0.8F;
            case 9:
                return 0.84F;
            case 10:
                return 0.9F;
            case 11:
                return 0.94F;
            case 12:
                return 1.0F;
            case 13:
                return 1.06F;
            case 14:
                return 1.12F;
            case 15:
                return 1.18F;
            case 16:
                return 1.26F;
            case 17:
                return 1.34F;
            case 18:
                return 1.42F;
            case 19:
                return 1.5F;
            case 20:
                return 1.6F;
            case 21:
                return 1.68F;
            case 22:
                return 1.78F;
            case 23:
                return 1.88F;
            case 24:
                return 2.0F;
        }
        return 0.0F;
    }

    public float getPitch() {
        return pitch;
    }

    public long getTick() {
        return tick;
    }

    public void play(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    @Override
    public String toString() {
        return "Note: " + " s=" + sound + ", v=" + volume + ", p=" + pitch;
    }
}
