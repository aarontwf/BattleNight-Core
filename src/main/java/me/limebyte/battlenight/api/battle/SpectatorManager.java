package me.limebyte.battlenight.api.battle;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface SpectatorManager {

    public Location addSpectator(Player player, boolean store);

    public Location addSpectator(Player player, Player target, boolean store);

    public void removeSpectator(Player player);

    public Set<String> getSpectators();

    public void addTarget(Player player);

    public void removeTarget(Player player);

    public Player getTarget(Player player);

    public void setTarget(Player player, Player target);

    public void cycleTarget(Player player);

}
