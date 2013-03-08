package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.util.sound.Note;
import me.limebyte.battlenight.core.util.sound.Song;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class BattleTimer extends Timer {

    private Battle battle;
    private static final int MINUTE = 60;
    private static final int COUNTDOWN_START = 10;

    public BattleTimer(Battle battle, long time) {
        super(time);
        this.battle = battle;
    }

    @Override
    public void onTimeChange(long time) {
        if (time == Song.battleEnd.length()) {
            Messenger.playSong(Song.battleEnd, true);
        }

        if (time % 10 == 0) {
            long timeSec = time / 10;
            if (timeSec <= COUNTDOWN_START) {
                Messenger.playSound(Sound.NOTE_PIANO, Note.convertPitch(18), true);
                Messenger.tellEveryone("" + ChatColor.RED + timeSec + "!", true);
            } else if (timeSec == MINUTE) {
                Messenger.tellEveryone(ChatColor.RED + "1 minute remaining!", true);
            } else if (timeSec % MINUTE == 0) {
                Messenger.tellEveryone(timeSec / MINUTE + " minutes remaining.", true);
            }
        }
    }

    @Override
    public void onTimerEnd() {
        battle.stop();
    }

}
