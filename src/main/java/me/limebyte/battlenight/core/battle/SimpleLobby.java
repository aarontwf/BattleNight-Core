package me.limebyte.battlenight.core.battle;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.event.lobby.LobbyJoinEvent;
import me.limebyte.battlenight.api.event.lobby.LobbyLeaveEvent;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.managers.ScoreManager.ScoreboardState;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.battle.battles.FFABattle;
import me.limebyte.battlenight.core.battle.battles.TDMBattle;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;
import me.limebyte.battlenight.core.util.Teleporter;
import me.limebyte.battlenight.core.util.player.BattlePlayer;
import me.limebyte.battlenight.core.util.player.Metadata;
import me.limebyte.battlenight.core.util.player.PlayerData;
import me.limebyte.battlenight.core.util.timers.LobbyTimer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SimpleLobby implements Lobby {

    private BattleNightAPI api;
    private Set<UUID> players;
    private Arena arena;
    private boolean starting = false;
    private LobbyTimer timer;
    private Random random;

    public SimpleLobby(BattleNightAPI api) {
        this.api = api;
        players = new HashSet<UUID>();
        timer = new LobbyTimer(api, this, 10L);
        random = new Random();
    }

    @Override
    public void addPlayer(Player player) {
        Messenger messenger = api.getMessenger();
        Battle battle = api.getBattle();

        if (players.contains(player.getUniqueId())) {
            messenger.tell(player, messenger.get("lobby.already-joined"));
            return;
        }

        if (battle != null) {
            if (battle.containsPlayer(player)) {
                messenger.tell(player, messenger.get("battle.already-joined"));
                return;
            }

            if (players.size() >= battle.getMaxPlayers()) {
                messenger.tell(player, Message.BATTLE_FULL);
                return;
            }
        }

        ArenaManager arenas = api.getArenaManager();
        if (!arenas.getLounge().isSet()) {
            messenger.tell(player, Message.WAYPOINTS_UNSET);
            return;
        }

        players.add(player.getUniqueId());

        PlayerData.store(player);
        api.getMessenger().log(Level.INFO, "Saved");
        PlayerData.reset(player);
        api.getMessenger().log(Level.INFO, "Resest");
        Teleporter.tp(player, arenas.getLounge());
        api.getMessenger().log(Level.INFO, "Teleported");
        api.getScoreManager().addPlayer(player);

        if (starting) {
            api.setPlayerClass(player, api.getClassManager().getRandomClass());
        }

        messenger.tell(player, messenger.get("lobby.join"));
        messenger.tellLobby(messenger.get("lobby.player-join"), player);

        Bukkit.getPluginManager().callEvent(new LobbyJoinEvent(this, player));
    }

    @Override
    public boolean contains(Player player) {
        return players.contains(player.getUniqueId());
    }

    @Override
    public Set<UUID> getPlayers() {
        return players;
    }

    @Override
    public boolean isStarting() {
        return starting;
    }

    @Override
    public void removePlayer(Player player) {
        Messenger messenger = api.getMessenger();

        if (!players.contains(player.getUniqueId())) {
            messenger.tell(player, messenger.get("lobby.not-in"));
            return;
        }

        players.remove(player.getUniqueId());

        api.getScoreManager().removePlayer(player);
        PlayerData.reset(player);
        PlayerData.restore(player, true, false);

        BattlePlayer bPlayer = BattlePlayer.get(player.getUniqueId());
        bPlayer.setReady(false);
        Metadata.remove(player, "kills");
        Metadata.remove(player, "deaths");
        Metadata.remove(player, "voted");

        Bukkit.getPluginManager().callEvent(new LobbyLeaveEvent(this, player));
    }

    public void start() {
        Battle battle = api.getBattle();

        for (UUID id : players) {
            Player player = Bukkit.getPlayer(id);
            if (player == null) {
                continue;
            }

            BattlePlayer bPlayer = BattlePlayer.get(player.getUniqueId());
            bPlayer.setReady(false);
            Metadata.remove(player, "vote");
            battle.addPlayer(player);
        }
        battle.start();
        players.clear();

        for (Arena arena : api.getArenaManager().getArenas()) {
            arena.setVotes(0);
        }

        starting = false;
    }

    @Override
    public void startBattle() {
        Messenger messenger = api.getMessenger();
        Battle battle = api.getBattle();
        ArenaManager manager = api.getArenaManager();

        if (battle != null && battle.isInProgress()) throw new IllegalStateException("Battle in progress!");
        battle = getNewBattle();
        api.setBattle(battle);

        if (players.size() < battle.getMinPlayers()) throw new IllegalStateException("Not enough players!");

        List<Arena> arenas = manager.getReadyArenas(1);
        int votes = 0;
        for (Arena a : manager.getReadyArenas(1)) {
            int v = a.getVotes();
            if (v > votes) {
                arenas.clear();
                votes = v;
            }
            if (v == votes) {
                arenas.add(a);
            }
        }

        if (arenas.isEmpty()) throw new IllegalStateException("No arenas!");

        arena = arenas.get(random.nextInt(arenas.size()));
        battle.setArena(arena);
        messenger.tellLobby(messenger.get("arena.chosen"), battle.getType(), arena);
        api.getScoreManager().setState(ScoreboardState.BATTLE);

        starting = true;
        timer.start();
    }

    private Battle getNewBattle() {
        String id = ConfigManager.get(Config.MAIN).getString("Battle.Type", "FFA");

        int duration = ConfigManager.get(Config.MAIN).getInt("Battle.Duration", 300);
        int minPlayers = ConfigManager.get(Config.MAIN).getInt("Battle.MinPlayers", 2);
        int maxPlayers = ConfigManager.get(Config.MAIN).getInt("Battle.MaxPlayers", 0);

        if (duration < 5) {
            duration = 5;
        }
        if (minPlayers < 2) {
            minPlayers = 2;
        }
        if (maxPlayers < 1) {
            maxPlayers = Integer.MAX_VALUE;
        }
        if (minPlayers > maxPlayers) {
            maxPlayers = minPlayers;
        }

        if (id.equalsIgnoreCase("TDM")) return new TDMBattle(api, duration, minPlayers, maxPlayers);

        return new FFABattle(api, duration, minPlayers, maxPlayers);
    }

    protected void addPlayerFromBattle(Player player) {
        Messenger messenger = api.getMessenger();

        if (players.contains(player.getUniqueId())) {
            messenger.tell(player, messenger.get("lobby.already-joined"));
            return;
        }

        ArenaManager arenas = api.getArenaManager();
        if (!arenas.getLounge().isSet()) {
            messenger.tell(player, Message.WAYPOINTS_UNSET);
            return;
        }

        players.add(player.getUniqueId());

        PlayerData.reset(player);
        Teleporter.tp(player, arenas.getLounge());

        api.getPlayerClass(player).equip(player);

        messenger.tell(player, messenger.get("lobby.join"));
        messenger.tellLobby(messenger.get("lobby.player-join"), player);
    }

}
