package me.limebyte.battlenight.core.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.managers.SpectatorManager;
import me.limebyte.battlenight.api.tosort.PlayerData;
import me.limebyte.battlenight.core.tosort.Messenger;
import me.limebyte.battlenight.core.tosort.Messenger.Message;
import me.limebyte.battlenight.core.tosort.SafeTeleporter;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CoreSpectatorManager implements SpectatorManager {

    private static Map<String, String> spectators = new HashMap<String, String>();
    private static List<String> targets = new ArrayList<String>();

    private BattleNightAPI api;

    public CoreSpectatorManager(BattleNightAPI api) {
        this.api = api;
    }

    @Override
    public Location addSpectator(Player player, boolean store) {
        if (targets.isEmpty()) return null;
        Player target = Bukkit.getPlayerExact(targets.get(0));
        if (target == null) return null;

        return addSpectator(player, target, store);
    }

    @Override
    public Location addSpectator(Player player, Player target, boolean store) {
        if (spectators.containsKey(player.getName()) || !targets.contains(target.getName())) return null;
        spectators.put(player.getName(), target.getName());
        if (store) {
            PlayerData.store(player);
        }
        PlayerData.reset(player);
        SafeTeleporter.tp(player, target.getLocation());
        player.setGameMode(GameMode.ADVENTURE);
        player.hidePlayer(target);

        for (String n : api.getBattle().getPlayers()) {
            if (Bukkit.getPlayerExact(n) != null) {
                Bukkit.getPlayerExact(n).hidePlayer(player);
            }
        }

        Messenger.tell(player, Message.WELCOME_SPECTATOR, target);
        return target.getLocation().clone();
    }

    @Override
    public void addTarget(Player player) {
        String name = player.getName();
        if (!targets.contains(name)) {
            targets.add(name);
        }
    }

    @Override
    public void cycleTarget(Player player) {
        if (!spectators.containsKey(player.getName())) return;

        if (targets.isEmpty()) {
            removeSpectator(player);
            return;
        }

        if (targets.size() == 1) return;

        int index = targets.indexOf(spectators.get(player.getName()));
        index++;
        if (index > targets.size() - 1) {
            index = 0;
        }
        spectators.put(player.getName(), targets.get(index));

        Player target = getTarget(player);

        SafeTeleporter.tp(player, target.getLocation());

        for (String n : api.getBattle().getPlayers()) {
            if (Bukkit.getPlayerExact(n) != null) {
                player.showPlayer(Bukkit.getPlayerExact(n));
            }
        }

        player.hidePlayer(target);

        Messenger.tell(player, Message.TARGET_CYCLED, target);
    }

    @Override
    public Set<String> getSpectators() {
        return spectators.keySet();
    }

    @Override
    public Player getTarget(Player player) {
        return Bukkit.getPlayerExact(spectators.get(player.getName()));
    }

    @Override
    public void removeSpectator(Player player) {
        if (!spectators.containsKey(player.getName())) return;
        spectators.remove(player.getName());
        PlayerData.restore(player, true, false);
        Messenger.tell(player, Message.GOODBYE_SPECTATOR);
    }

    @Override
    public void removeTarget(Player player) {
        String name = player.getName();

        if (targets.remove(name)) {
            for (String spectator : getSpectators()) {
                if (spectators.get(spectator) == name) {
                    cycleTarget(Bukkit.getPlayerExact(spectator));
                }
            }
        }
    }

    @Override
    public void setTarget(Player player, Player target) {
        if (!spectators.containsKey(player.getName()) || spectators.containsKey(target.getName()) || !targets.contains(target.getName())) return;
        spectators.put(player.getName(), target.getName());
    }
}
