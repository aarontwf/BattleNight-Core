package me.limebyte.battlenight.core.util.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.event.battle.BattleDeathEvent;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.listeners.HealthListener.DeathCause;
import me.limebyte.battlenight.core.util.Teleporter;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BattlePlayer {

    private static final int RESPAWN_TIME = 2;
    private static Map<UUID, BattlePlayer> players = new HashMap<UUID, BattlePlayer>();

    private static final Map<DamageCause, String> deathCauses;
    static {
        deathCauses = new HashMap<DamageCause, String>();
        deathCauses.put(DamageCause.BLOCK_EXPLOSION, "$p blew up.");
        deathCauses.put(DamageCause.CONTACT, "$p was pricked to death.");
        deathCauses.put(DamageCause.CUSTOM, "$p was killed by an unknown source.");
        deathCauses.put(DamageCause.DROWNING, "$p drowned.");
        deathCauses.put(DamageCause.ENTITY_ATTACK, "$k killed $p.");
        deathCauses.put(DamageCause.ENTITY_EXPLOSION, "$p blew up.");
        deathCauses.put(DamageCause.FALL, "$p fell to their death.");
        deathCauses.put(DamageCause.FALLING_BLOCK, "$p was crushed.");
        deathCauses.put(DamageCause.FIRE, "$p burnt to a crisp.");
        deathCauses.put(DamageCause.FIRE_TICK, "$p burnt to a crisp.");
        deathCauses.put(DamageCause.LAVA, "$p tried to swim in lava.");
        deathCauses.put(DamageCause.LIGHTNING, "$p got struck by lightning.");
        deathCauses.put(DamageCause.MAGIC, "$k killed $p with magic.");
        deathCauses.put(DamageCause.MELTING, "$p melted away.");
        deathCauses.put(DamageCause.POISON, "$k poisoned $p.");
        deathCauses.put(DamageCause.PROJECTILE, "$k shot $p.");
        deathCauses.put(DamageCause.STARVATION, "$p starved.");
        deathCauses.put(DamageCause.SUFFOCATION, "$p suffocated.");
        deathCauses.put(DamageCause.SUICIDE, "$p commited suicide.");
        deathCauses.put(DamageCause.THORNS, "$k pricked $p to death.");
        deathCauses.put(DamageCause.VOID, "$p fell into the void.");
        deathCauses.put(DamageCause.WITHER, "$p withered away.");
    }

    private UUID id;
    private PlayerStats stats;

    private boolean alive;
    private boolean ready;
    private int respawnTaskID;

    private BattlePlayer(UUID id) {
        this.id = id;
        this.stats = new PlayerStats(id);
        this.alive = true;
        this.ready = false;
    }

    public static BattlePlayer get(UUID id) {
        if (players.get(id) == null) {
            players.put(id, new BattlePlayer(id));
        }
        return players.get(id);
    }

    public static Map<UUID, BattlePlayer> getPlayers() {
        return players;
    }

    private static void killFeed(Player player, Player killer, DamageCause cause, DeathCause accolade) {
        Messenger messenger = BattleNight.instance.getAPI().getMessenger();

        String causeMsg = deathCauses.get(cause);
        if (accolade != null) {
            causeMsg = accolade.getMessage();
        }
        if (causeMsg == null) {
            causeMsg = "$p died.";
        }

        String deathMessage = causeMsg.replace("$p", messenger.getColouredName(player));

        if (killer != null) {
            deathMessage = deathMessage.replace("$k", messenger.getColouredName(killer));
        }

        messenger.tellBattle(deathMessage);
    }

    public UUID getUUID() {
        return this.id;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        boolean changed = this.ready != ready;
        this.ready = ready;
        if (!ready) return;

        BattleNightAPI api = BattleNight.instance.getAPI();
        Messenger messenger = api.getMessenger();
        Lobby lobby = api.getLobby();
        Battle battle = api.getBattle();
        Player player = Bukkit.getPlayer(id);

        if (!lobby.contains(player)) return;

        if (api.getPlayerClass(player) == null) {
            messenger.tell(player, messenger.get("class.must-equip"));
            return;
        }

        if (battle != null && battle.isInProgress()) {
            Metadata.remove(player, "voted");
            Metadata.remove(player, "vote");
            battle.addPlayer(player);
            Teleporter.tp(player, battle.getArena().getRandomSpawnPoint());
            lobby.getPlayers().remove(player.getUniqueId());
        } else {
            if (changed) messenger.tellLobby(messenger.get("lobby.player-ready"), player);
            if (!lobby.isStarting()) {
                Set<UUID> waiting = new HashSet<UUID>(lobby.getPlayers());
                Set<String> names = new HashSet<String>();
                Iterator<UUID> it = waiting.iterator();
                while (it.hasNext()) {
                    UUID uuid = it.next();
                    BattlePlayer p = BattlePlayer.get(uuid);
                    if (p.isReady()) {
                        it.remove();
                    } else {
                        names.add(Bukkit.getPlayer(uuid).getName());
                    }
                }

                if (waiting.isEmpty()) {
                    try {
                        lobby.startBattle();
                    } catch (IllegalStateException e) {
                        messenger.tellLobby(e.getMessage());
                    }
                } else {
                    String list = names.toString().replaceAll("[,]([^,]*)$", " and$1");
                    messenger.tellLobby(messenger.get("lobby.waiting-for"), list.replaceAll("\\[|\\]", ""));
                }
            }
        }
    }

    public void kill(Battle battle, Player killer, DamageCause cause, DeathCause accolade) {
        if (!alive) return;
        alive = false;
        Player player = Bukkit.getPlayer(id);
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.hidePlayer(player);
        }

        boolean suicide = true;

        if (killer != null && killer != player) {
            BattlePlayer.get(killer.getUniqueId()).getStats().addKill(false);
            killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 20f, 1f);
            suicide = false;
        }

        killFeed(player, killer, cause, accolade);
        stats.addDeath(suicide);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed(0);
        BukkitTask task = new RespawnTask(player).runTaskTimer(BattleNight.instance, 0L, 1L);
        respawnTaskID = task.getTaskId();

        Bukkit.getPluginManager().callEvent(new BattleDeathEvent(battle, player, killer));
    }

    public void revive() {
        if (alive) return;
        alive = true;

        Bukkit.getScheduler().cancelTask(respawnTaskID);
        Player player = Bukkit.getPlayer(id);
        Battle battle = BattleNight.instance.getAPI().getBattle();

        if (battle != null) battle.respawn(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }
    }

    class RespawnTask extends BukkitRunnable {
        private static final float liftHeight = 1.0F;

        private Player player;
        private int timeRemaining;
        private float liftAmount;
        private boolean moveUp;

        public RespawnTask(Player player) {
            this.player = player;
            timeRemaining = RESPAWN_TIME * 20;
            liftAmount = liftHeight / timeRemaining;
            moveUp = player.getEyeLocation().clone().add(0, liftHeight, 0).getBlock().isEmpty();
        }

        @Override
        public void run() {

            if (moveUp) {
                Teleporter.telePass.add(player.getUniqueId());
                player.teleport(player.getLocation().add(0, liftAmount, 0), TeleportCause.PLUGIN);
                Teleporter.telePass.remove(player.getUniqueId());
            }

            if (timeRemaining <= 0) {
                revive();
                return;
            }

            timeRemaining--;
        }
    }

}
