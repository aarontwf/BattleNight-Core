package me.limebyte.battlenight.core.battle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.event.battle.BattleEndEvent;
import me.limebyte.battlenight.api.event.battle.BattleRespawnEvent;
import me.limebyte.battlenight.api.event.battle.BattleStartEvent;
import me.limebyte.battlenight.api.managers.ScoreManager.ScoreboardState;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.Timer;
import me.limebyte.battlenight.core.util.Teleporter;
import me.limebyte.battlenight.core.util.player.BattlePlayer;
import me.limebyte.battlenight.core.util.player.PlayerData;
import me.limebyte.battlenight.core.util.timers.BattleTimer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class SimpleBattle implements Battle {

    public BattleNightAPI api;

    private BattleTimer timer;
    private int minPlayers;
    private int maxPlayers;

    private Arena arena;
    protected boolean inProgress = false;

    private HashSet<String> players = new HashSet<String>();

    public SimpleBattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        this.api = api;

        timer = new BattleTimer(api, this, duration);

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public boolean addPlayer(Player player) {
        getPlayers().add(player.getName());

        if (!arena.getTexturePack().isEmpty()) {
            player.setTexturePack(arena.getTexturePack());
        }
        return true;
    }

    @Override
    public boolean containsPlayer(Player player) {
        return getPlayers().contains(player.getName());
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * @return the players
     */
    @Override
    public Set<String> getPlayers() {
        return players;
    }

    @Override
    public Timer getTimer() {
        return timer;
    }

    /**
     * @return if in progress
     */
    @Override
    public boolean isInProgress() {
        return inProgress;
    }

    @Override
    public boolean removePlayer(Player player) {
        if (!containsPlayer(player)) return false;
        PlayerData.reset(player);
        PlayerData.restore(player, true, false);
        api.setPlayerClass(player, null);
        getPlayers().remove(player.getName());
        api.getScoreManager().removePlayer(player);
        BattlePlayer.get(player.getName()).getStats().reset();

        if (shouldEnd()) {
            stop();
        }
        return true;
    }

    @Override
    public void respawn(Player player) {
        if (!containsPlayer(player)) return;

        api.getMessenger().debug(Level.INFO, "Respawning " + player.getName() + "...");
        PlayerData.reset(player);
        api.getPlayerClass(player).equip(player);

        BattleRespawnEvent event = new BattleRespawnEvent(this, player, getArena().getRandomSpawnPoint());
        Bukkit.getPluginManager().callEvent(event);
        Teleporter.tp(player, event.getWaypoint());
    }

    @Override
    public void setArena(Arena arena) {
        if (isInProgress()) return;
        this.arena = arena;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < getMinPlayers()) return;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void setMinPlayers(int minPlayers) {
        if (getMinPlayers() < 1) return;
        this.minPlayers = minPlayers;
    }

    public boolean shouldEnd() {
        return getPlayers().size() < minPlayers;
    }

    @Override
    public boolean start() {
        if (isInProgress()) return false;

        Messenger messenger = api.getMessenger();

        teleportAllToSpawn();

        timer.start();
        inProgress = true;

        Bukkit.getPluginManager().callEvent(new BattleStartEvent(this));
        messenger.tellBattle(Message.BATTLE_STARTED);

        return true;
    }

    @Override
    public boolean stop() {
        if (timer.isRunning()) {
            timer.stop();
        }

        if (inProgress) {
            api.getMessenger().tellBattle(getWinMessage());
        }

        Iterator<String> pIt = getPlayers().iterator();
        while (pIt.hasNext()) {
            Player player = toPlayer(pIt.next());
            if (player == null) {
                pIt.remove();
                continue;
            }

            BattlePlayer bPlayer = BattlePlayer.get(player.getName());
            bPlayer.revive();
            bPlayer.getStats().reset();

            ((SimpleLobby) api.getLobby()).addPlayerFromBattle(player);

            pIt.remove();
        }

        api.getScoreManager().setState(ScoreboardState.VOTING);

        arena = null;
        inProgress = false;

        Bukkit.getPluginManager().callEvent(new BattleEndEvent(this));
        return true;
    }

    protected String getWinMessage() {
        String message;

        List<String> leading = new ArrayList<String>();
        int leadingScore = Integer.MIN_VALUE;
        Map<String, BattlePlayer> bPlayers = BattlePlayer.getPlayers();

        for (String name : players) {
            int score = bPlayers.get(name).getStats().getScore();
            if (score < leadingScore) {
                continue;
            }

            if (score > leadingScore) {
                leading.clear();
                leadingScore = score;
            }
            leading.add(name);
        }

        if (leading.isEmpty()) {
            message = Message.DRAW.getMessage();
        } else if (leading.size() == 1) {
            message = api.getMessenger().format(Message.PLAYER_WON, leading.get(0), leadingScore);
        } else {
            message = api.getMessenger().format(Message.PLAYER_WON, leading, leadingScore);
        }

        return message;
    }

    /* ------ */
    /* Events */
    /* ------ */

    protected void teleportAllToSpawn() {
        ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>(getArena().getSpawnPoints());
        ArrayList<Waypoint> free = new ArrayList<Waypoint>(waypoints);
        Random random = new Random();

        for (String name : getPlayers()) {
            Player player = toPlayer(name);
            if (player == null || !player.isOnline()) {
                continue;
            }

            if (free.size() <= 0) {
                free = new ArrayList<Waypoint>(waypoints);
            }

            int id = random.nextInt(free.size());
            Teleporter.queue(player, free.get(id));
            free.remove(id);
        }

        Teleporter.startTeleporting();
    }

    protected Player toPlayer(String name) {
        return Bukkit.getPlayerExact(name);
    }
}
