package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
            Messenger.playSound(Sound.NOTE_PLING, 13, true);
            Messenger.tellEveryone("" + ChatColor.RED + time + "!", true);
            Player player = Bukkit.getPlayerExact("limebyte");
            if (player != null) player.getWorld().playSound(player.getLocation(), Sound.NOTE_PLING, 20, 10);
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
