package me.limebyte.battlenight.api;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public interface Battle {

    public boolean start();

    public boolean end();

    public boolean isInProgress();

    public boolean addPlayer(Player player);

    public boolean removePlayer(Player player);

    public boolean containsPlayer(Player player);

    public Set<String> getPlayers();

    public void onPlayerDamage(EntityDamageByEntityEvent event);

    public void onPlayerDeath(PlayerDeathEvent event);

    public void onPlayerRespawn(PlayerRespawnEvent event);

    public boolean addSpectator(Player player);

    public boolean removeSpectator(Player player);

    public boolean containsSpectator(Player player);

    public Set<String> getSpectators();

    public Player getLeadingPlayer();

    public Arena getArena();

    public boolean setArena(Arena arena);

}
