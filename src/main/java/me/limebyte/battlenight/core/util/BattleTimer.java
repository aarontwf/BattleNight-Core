package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.battle.Battle;

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
        if (time <= COUNTDOWN_START) {
            Messenger.playSound(Sound.NOTE_PLING, 3, true);
            Messenger.tellEveryone("" + ChatColor.RED + time + "!", true);
        } else if (time == MINUTE) {
            Messenger.tellEveryone(ChatColor.RED + "1 minute remaining!", true);
        } else if (time % MINUTE == 0) {
            Messenger.tellEveryone(time / MINUTE + " minutes remaining.", true);
        }
    }

    @Override
    public void onTimerEnd() {
        Messenger.playSound(Sound.NOTE_PLING, 1, true);
        battle.stop();
    }

}
