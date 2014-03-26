package org.battlenight.core.timer;

import org.battlenight.api.game.Lobby;
import org.battlenight.api.message.Messenger;
import org.battlenight.core.BattleNight;
import org.battlenight.core.game.SimpleLobby;

public class LobbyTimer extends SimpleTimer {

    private Lobby lobby;
    private static final int COUNTDOWN_START = 10;

    public LobbyTimer(BattleNight plugin, SimpleLobby lobby, long time) {
        super(plugin, time);
        this.lobby = lobby;
    }

    @Override
    public void onTimeChange(long time) {
        Messenger messenger = getPlugin().getMessenger();

        if (time > 0 && time % 60 == 0) {
            messenger.sendLobby("lobby.countdown-minutes", time / 60);
        } else if (time <= COUNTDOWN_START) {
            messenger.sendLobby("lobby.countdown-seconds", time);
        }
    }

    @Override
    public void onTimerEnd() {
        start();
        lobby.startBattle();
    }

}
