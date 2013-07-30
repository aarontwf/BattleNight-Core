package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.battle.SimpleLobby;
import me.limebyte.battlenight.core.tosort.Note;

import org.bukkit.Sound;

public class LobbyTimer extends SimpleTimer {

    private BattleNightAPI api;
    private SimpleLobby lobby;
    private static final int COUNTDOWN_START = 5;

    public LobbyTimer(BattleNightAPI api, SimpleLobby lobby, long time) {
        super(time);
        this.api = api;
        this.lobby = lobby;
    }

    @Override
    public void onTimeChange(long time) {
        Messenger messenger = api.getMessenger();

        if (time % 10 == 0) {
            long timeSec = time / 10;

            if (timeSec <= COUNTDOWN_START) {
                messenger.playSound(Sound.NOTE_PIANO, Note.convertPitch(18));
                messenger.tellLobby(Message.LOBBY_COUNTDOWN, timeSec);
            }
        }
    }

    @Override
    public void onTimerEnd() {
        lobby.start();
    }

}