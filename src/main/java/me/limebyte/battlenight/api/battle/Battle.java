package me.limebyte.battlenight.api.battle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.event.BattleDeathEvent;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.BattleTimer;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.SafeTeleporter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Battle {

    public BattleNightAPI api;

    private Arena arena;
    private BattleTimer timer;

    protected boolean inProgress = false;
    private int minPlayers = 2;
    private int maxPlayers = Integer.MAX_VALUE;

    private HashSet<String> players = new HashSet<String>();
    private Set<String> leadingPlayers = new HashSet<String>();

    public Battle(int duration) {
        timer = new BattleTimer(this, duration);
    }

    /* --------------- */
    /* General Methods */
    /* --------------- */

    @SuppressWarnings("unchecked")
    public boolean start() {
        if (isInProgress()) return false;
        if (getPlayers().size() < getMinPlayers()) return false;
        if (getPlayers().size() > getMaxPlayers()) return false;
        if (getArena() == null || !getArena().isSetup(1) || !getArena().isEnabled()) return false;
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

    /**
     * Adds the specified {@link Player} to the battle. This will return false
     * if it is unsuccessful.
     * 
     * @param player the Player to add
     * @return true if successful
     */
    public boolean addPlayer(Player player) {
        if (isInProgress()) {
            Messenger.tell(player, Message.BATTLE_IN_PROGRESS);
            return false;
        }

        if (containsPlayer(player)) {
            Messenger.tell(player, Message.ALREADY_IN_BATTLE);
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

    /**
     * Removes the specified {@link Player} to the battle. This will return
     * false if it is unsuccessful.
     * 
     * @param player the Player to remove
     * @return true if successful
     */
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

    public void respawn(Player player) {
        if (!containsPlayer(player)) return;
        Messenger.debug(Level.INFO, "Respawning " + player.getName() + "...");
        PlayerData.reset(player);
        api.getPlayerClass(player).equip(player);
        SafeTeleporter.tp(player, getArena().getRandomSpawnPoint().getLocation());
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

    /**
     * Returns the {@link Arena} that is set for this battle.
     * 
     * @return the arena
     * @see Arena
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Sets the {@link Arena} that will be used for this battle. The arena will
     * not be set if this battle is in progress.
     * 
     * @param arena the arena to set
     * @see Arena
     */
    public void setArena(Arena arena) {
        if (isInProgress()) return;
        this.arena = arena;
    }

    /**
     * Returns the minimum amount of players the battle requires before it can
     * be started.
     * 
     * @return the minPlayers
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Sets the minimum amount of players the battle requires before it can be
     * started. This cannot be set below one.
     * 
     * @param minPlayers the minPlayers to set
     */
    public void setMinPlayers(int minPlayers) {
        if (getMinPlayers() < 1) return;
        this.minPlayers = minPlayers;
    }

    /**
     * Returns the maximum amount of players the battle can have. By default
     * this is set to {@link Integer.MAX_VALUE}.
     * 
     * @return the maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Sets the maximum amount of players the battle can have. Setting this
     * value will prevent players from joining if the battle is full. This
     * cannot be set to a value that is less than the minimum.
     * 
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxPlayers) {
        if (maxPlayers < getMinPlayers()) return;
        this.maxPlayers = maxPlayers;
    }

    public BattleTimer getTimer() {
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

    public void onPlayerDeath(BattleDeathEvent event) {
        Player player = event.getPlayer();
        Player killer = player.getKiller();

        if (killer != null && killer != player) addKill(killer);

        int deaths = Metadata.getInt(player, "deaths");
        Metadata.set(player, "deaths", ++deaths);

        event.setCancelled(true);
    }

    protected void addKill(Player player) {
        int kills = Metadata.getInt(player, "kills") + 1;
        int leadingKills = 0;

        Player leader = toPlayer(leadingPlayers.iterator().next());
        if (leader != null) leadingKills = Metadata.getInt(leader, "kills");

        Metadata.set(player, "kills", kills);

        if (leadingKills > kills) return;
        if (leadingKills < kills) leadingPlayers.clear();

        leadingPlayers.add(player.getName());
    }
}
