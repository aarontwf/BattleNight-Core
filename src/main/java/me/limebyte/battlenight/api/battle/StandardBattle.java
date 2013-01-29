package me.limebyte.battlenight.api.battle;

import java.util.logging.Level;

import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class StandardBattle extends Battle {

    public Location toSpectator(Player player, boolean death) {
        if (!containsPlayer(player)) return null;
        Messenger.debug(Level.INFO, "To spectator " + player.getName());
        Location loc;

        api.setPlayerClass(player, null);
        getPlayers().remove(player.getName());
        if (!death) PlayerData.reset(player);
        Metadata.remove(player, "lives");
        Metadata.remove(player, "ready");

        if (isInProgress() && getPlayers().size() >= getMinPlayers()) {
            getSpectators().add(player.getName());
            player.setGameMode(GameMode.ADVENTURE);
            player.setAllowFlight(true);
            for (String n : getPlayers()) {
                if (Bukkit.getPlayerExact(n) != null) {
                    Bukkit.getPlayerExact(n).hidePlayer(player);
                }
            }

            loc = Bukkit.getPlayerExact((String) getPlayers().toArray()[0]).getLocation();
        } else {
            loc = PlayerData.getSavedLocation(player);
            if (!death) PlayerData.restore(player, true, false);
            Messenger.tellEveryone(((String) getPlayers().toArray()[0]) + " won the battle!", true);
            stop();
        }
        return loc;
    }
}
