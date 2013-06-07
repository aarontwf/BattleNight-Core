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
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.Timer;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.tosort.PlayerData;
import me.limebyte.battlenight.core.tosort.SafeTeleporter;
import me.limebyte.battlenight.core.util.BattlePlayer;
import me.limebyte.battlenight.core.util.BattleScorePane;
import me.limebyte.battlenight.core.util.BattleTimer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class SimpleBattle implements Battle {

    public BattleNightAPI api;

    private BattleTimer timer;
    private BattleScorePane scoreboard;
    private int minPlayers;
    private int maxPlayers;

    private Arena arena;
    protected boolean inProgress = false;

    private HashSet<String> players = new HashSet<String>();

    public SimpleBattle(BattleNightAPI api, int duration, int minPlayers, int maxPlayers) {
        this.api = api;

        timer = new BattleTimer(api, this, duration);
        scoreboard = new BattleScorePane(this);

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public boolean addPlayer(Player player) {
        getPlayers().add(player.getName());
        getScoreboard().addPlayer(player);

        api.getSpectatorManager().addTarget(player);

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
        getScoreboard().removePlayer(player);
        api.getSpectatorManager().removeTarget(player);
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
        SafeTeleporter.tp(player, getArena().getRandomSpawnPoint());
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
        int constraint = inProgress ? 2 : 1;
        return getPlayers().size() < constraint;
    }

    @Override
    public boolean start() {
        if (isInProgress()) return false;

        Messenger messenger = api.getMessenger();

        teleportAllToSpawn();

        timer.start();
        inProgress = true;

        messenger.tellBattle(Message.BATTLE_STARTED);

        SignListener.cleanSigns();
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

            api.getSpectatorManager().removeTarget(player);

            getScoreboard().removePlayer(player);

            ((SimpleLobby) api.getLobby()).addPlayerFromBattle(player);

            pIt.remove();
        }

        SpectatorManager spectatorManager = api.getSpectatorManager();
        for (String name : spectatorManager.getSpectators()) {
            Player player = toPlayer(name);
            if (player == null) {
                continue;
            }
            spectatorManager.removeSpectator(player);
        }

        api.getBattleManager().getNewBattle();

        arena = null;
        inProgress = false;
        return true;
    }

    public Location toSpectator(Player player, boolean death) {
        if (!containsPlayer(player)) return null;
        Messenger messenger = api.getMessenger();

        messenger.debug(Level.INFO, "To spectator " + player.getName());
        Location loc;

        api.setPlayerClass(player, null);
        getPlayers().remove(player.getName());
        api.getSpectatorManager().removeTarget(player);
        if (!death) {
            PlayerData.reset(player);
        }

        if (shouldEnd()) {
            loc = PlayerData.getSavedLocation(player);
            if (!death) {
                PlayerData.restore(player, true, false);
            }

            String winMessage = getWinMessage();
            messenger.tell(player, winMessage);
            messenger.tellBattle(winMessage);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleNight.instance, new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, 1L);
        } else {
            loc = api.getSpectatorManager().addSpectator(player, false);
            messenger.tellBattle(player.getDisplayName() + " is now a spectator.");
        }
        return loc;
    }

    protected String getWinMessage() {
        String message;

        List<String> leading = new ArrayList<String>();
        int leadingScore = Integer.MIN_VALUE;
        Map<String, BattlePlayer> bPlayers = BattlePlayer.getPlayers();

        for (String name : players) {
            int score = bPlayers.get(name).getStats().getScore();
            if (score < leadingScore) continue;

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

    // TODO Add to API
    public BattleScorePane getScoreboard() {
        return scoreboard;
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
            SafeTeleporter.queue(player, free.get(id));
            free.remove(id);
        }

        SafeTeleporter.startTeleporting();
    }

    protected Player toPlayer(String name) {
        Player player = Bukkit.getPlayerExact(name);
        return player;
    }
}
