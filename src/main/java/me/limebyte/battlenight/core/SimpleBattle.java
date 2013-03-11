package me.limebyte.battlenight.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.tosort.Arena;
import me.limebyte.battlenight.api.tosort.PlayerData;
import me.limebyte.battlenight.api.tosort.Waypoint;
import me.limebyte.battlenight.api.util.Timer;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.util.BattleTimer;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.SafeTeleporter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class SimpleBattle implements Battle {

    public BattleNightAPI api;

    private BattleTimer timer;
    private int minPlayers;
    private int maxPlayers;

    private Arena arena;
    protected boolean inProgress = false;

    private HashSet<String> players = new HashSet<String>();
    private Set<String> leadingPlayers = new HashSet<String>();

    public SimpleBattle(int duration, int minPlayers, int maxPlayers) {
        timer = new BattleTimer(this, duration);
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    /* --------------- */
    /* General Methods */
    /* --------------- */

    @SuppressWarnings("unchecked")
    public boolean start() {
        if (isInProgress()) return false;
        if (getArena() == null || !getArena().isSetup(1) || !getArena().isEnabled()) return false;

        if (getPlayers().size() < getMinPlayers()) {
            Messenger.tellEveryone(true, Message.NOT_ENOUGH_PLAYERS, getMinPlayers() - getPlayers().size());
            return false;
        }

        if (!onStart()) return false;

        Iterator<String> it = getPlayers().iterator();
        while (it.hasNext()) {
            Player player = toPlayer(it.next());
            if (player == null) {
                it.remove();
                continue;
            }

            Metadata.remove(player, "ready");
            Metadata.set(player, "kills", 0);
            Metadata.set(player, "deaths", 0);
            api.getSpectatorManager().addTarget(player);
        }

        leadingPlayers = (HashSet<String>) players.clone();

        teleportAllToSpawn();

        timer.start();
        inProgress = true;

        Messenger.tellEveryone(true, Message.BATTLE_STARTED);

        SignListener.cleanSigns();
        return true;
    }

    public boolean stop() {
        if (!onStop()) return false;

        if (timer.isRunning()) timer.stop();

        if (inProgress) Messenger.tellEveryone(getWinMessage(), true);

        Iterator<String> pIt = getPlayers().iterator();
        while (pIt.hasNext()) {
            Player player = toPlayer(pIt.next());
            if (player == null) {
                pIt.remove();
                continue;
            }

            PlayerData.reset(player);
            PlayerData.restore(player, true, false);
            api.setPlayerClass(player, null);
            Metadata.remove(player, "kills");
            Metadata.remove(player, "deaths");
            api.getSpectatorManager().removeTarget(player);
            pIt.remove();
        }

        SpectatorManager spectatorManager = api.getSpectatorManager();
        for (String name : spectatorManager.getSpectators()) {
            Player player = toPlayer(name);
            if (player == null) continue;
            spectatorManager.removeSpectator(player);
        }

        leadingPlayers.clear();
        arena = null;
        inProgress = false;
        return true;
    }

    public boolean addPlayer(Player player) {
        if (isInProgress()) {
            Messenger.tell(player, Message.BATTLE_IN_PROGRESS);
            return false;
        }

        if (containsPlayer(player)) {
            Messenger.tell(player, Message.ALREADY_IN_BATTLE);
            return false;
        }

        if (getPlayers().size() + 1 > getMaxPlayers()) {
            Messenger.tell(player, Message.BATTLE_FULL);
            return false;
        }

        ArenaManager arenaManager = api.getArenaManager();

        if (!arenaManager.getLounge().isSet()) {
            Messenger.tell(player, Message.WAYPOINTS_UNSET);
            return false;
        }

        if (getArena() == null) {
            if (arenaManager.getReadyArenas(1).isEmpty()) {
                Messenger.tell(player, Message.NO_ARENAS);
                return false;
            }
            setArena(arenaManager.getRandomArena(1));
        }

        PlayerData.store(player);
        PlayerData.reset(player);
        getPlayers().add(player.getName());
        SafeTeleporter.tp(player, arenaManager.getLounge().getLocation());
        Messenger.tell(player, Message.JOINED_BATTLE, arena);
        Messenger.tellEveryoneExcept(player, true, Message.PLAYER_JOINED_BATTLE, player);
        if (!arena.getTexturePack().isEmpty()) player.setTexturePack(arena.getTexturePack());
        return true;
    }

    public boolean removePlayer(Player player) {
        if (!containsPlayer(player)) return false;
        PlayerData.reset(player);
        PlayerData.restore(player, true, false);
        api.setPlayerClass(player, null);
        getPlayers().remove(player.getName());
        Metadata.remove(player, "ready");
        Metadata.remove(player, "kills");
        Metadata.remove(player, "deaths");
        api.getSpectatorManager().removeTarget(player);

        if (shouldEnd()) stop();
        return true;
    }

    public Location respawn(Player player) {
        if (!containsPlayer(player)) return null;
        Messenger.debug(Level.INFO, "Respawning " + player.getName() + "...");
        PlayerData.reset(player);

        Location loc = getArena().getRandomSpawnPoint().getLocation();
        api.getPlayerClass(player).equip(player);
        SafeTeleporter.tp(player, loc);
        return loc.add(0, 0.5, 0);
    }

    public Location toSpectator(Player player, boolean death) {
        if (!containsPlayer(player)) return null;
        Messenger.debug(Level.INFO, "To spectator " + player.getName());
        Location loc;

        api.setPlayerClass(player, null);
        getPlayers().remove(player.getName());
        api.getSpectatorManager().removeTarget(player);
        if (!death) PlayerData.reset(player);
        Metadata.remove(player, "ready");

        if (shouldEnd()) {
            loc = PlayerData.getSavedLocation(player);
            if (!death) PlayerData.restore(player, true, false);

            String winMessage = getWinMessage();
            Messenger.tell(player, winMessage);
            Messenger.tellEveryone(winMessage, true);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(BattleNight.instance, new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, 1L);
        } else {
            loc = api.getSpectatorManager().addSpectator(player, false);
            Messenger.tellEveryone(player.getDisplayName() + " is now a spectator.", true);
        }
        return loc;
    }

    /* --------------- */
    /* Utility Methods */
    /* --------------- */

    protected Player toPlayer(String name) {
        Player player = Bukkit.getPlayerExact(name);
        return player;
    }

    protected void teleportAllToSpawn() {
        @SuppressWarnings("unchecked")
        List<Waypoint> waypoints = (ArrayList<Waypoint>) getArena().getSpawnPoints().clone();
        List<Waypoint> free = waypoints;
        Random random = new Random();

        for (String name : getPlayers()) {
            Player player = toPlayer(name);
            if (player == null || !player.isOnline()) continue;

            if (free.size() <= 0) free = waypoints;

            int id = random.nextInt(free.size());
            SafeTeleporter.tp(player, free.get(id).getLocation());
            free.remove(id);
        }
    }

    public boolean shouldEnd() {
        int constraint = inProgress ? 2 : 1;
        return getPlayers().size() < constraint;
    }

    /* ------------------- */
    /* Getters and Setters */
    /* ------------------- */

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        if (isInProgress()) return;
        this.arena = arena;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        if (getMinPlayers() < 1) return;
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < getMinPlayers()) return;
        this.maxPlayers = maxPlayers;
    }

    public Timer getTimer() {
        return timer;
    }

    /**
     * @return if in progress
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * @return the players
     */
    public Set<String> getPlayers() {
        return players;
    }

    public boolean containsPlayer(Player player) {
        return getPlayers().contains(player.getName());
    }

    public Set<String> getLeadingPlayers() {
        return leadingPlayers;
    }

    protected String getWinMessage() {
        String message;
        Set<String> leading = getLeadingPlayers();

        if (leading.isEmpty()) {
            message = Message.DRAW.getMessage();
        } else if (leading.size() == 1) {
            message = Messenger.format(Message.PLAYER_WON, leading.toArray()[0]);
        } else {
            message = Messenger.format(Message.PLAYER_WON, leading);
        }

        return message;
    }

    /* ------ */
    /* Events */
    /* ------ */

    public abstract boolean onStart();

    public abstract boolean onStop();

    public void addKill(Player player) {
        int kills = Metadata.getInt(player, "kills") + 1;
        int leadingKills = 0;

        Player leader = toPlayer(leadingPlayers.iterator().next());
        if (leader != null) leadingKills = Metadata.getInt(leader, "kills");

        Metadata.set(player, "kills", kills);

        if (leadingKills > kills) return;
        if (leadingKills < kills) leadingPlayers.clear();

        leadingPlayers.add(player.getName());
    }

    public void addDeath(Player player) {
        int deaths = Metadata.getInt(player, "deaths");
        Metadata.set(player, "deaths", ++deaths);
    }
}
