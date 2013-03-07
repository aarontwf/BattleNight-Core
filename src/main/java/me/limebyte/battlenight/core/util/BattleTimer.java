package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.sound.Note;
import me.limebyte.battlenight.core.util.sound.Song;
import me.limebyte.battlenight.core.util.sound.SongLoader;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

public class BattleTimer extends Timer {

    private Battle battle;
    private Song endSong;
    private static final int MINUTE = 60;
    private static final int COUNTDOWN_START = 10;

    public BattleTimer(Battle battle, long time) {
        super(time);
        this.battle = battle;

        SongLoader loader = new SongLoader(BattleNight.instance);
        endSong = loader.load("battle-end");
    }

    @Override
    public void onTimeChange(long time) {
        if (time == endSong.getDuration()) {
            Messenger.playSong(endSong, true);
        }

        if (time <= COUNTDOWN_START) {
            Messenger.playSound(Sound.NOTE_PIANO, Note.convertPitch(18), true);
            Messenger.tellEveryone("" + ChatColor.RED + time + "!", true);
        } else if (time == MINUTE) {
            Messenger.tellEveryone(ChatColor.RED + "1 minute remaining!", true);
        } else if (time % MINUTE == 0) {
            Messenger.tellEveryone(time / MINUTE + " minutes remaining.", true);
        }
    }

    @Override
    public void onTimerEnd() {
        battle.stop();
    }

}
