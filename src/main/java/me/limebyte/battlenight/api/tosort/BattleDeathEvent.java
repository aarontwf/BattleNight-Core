package me.limebyte.battlenight.api.tosort;

import me.limebyte.battlenight.api.battle.Battle;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class BattleDeathEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Battle battle;
    private Location respawnLocation;

    public BattleDeathEvent(Battle battle, Player player) {
        super(player);
        this.battle = battle;
        this.respawnLocation = battle.getArena().getRandomSpawnPoint().getLocation();
    }

    public Battle getBattle() {
        return battle;
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public void setRespawnLocation(Location respawnLocation) {
        this.respawnLocation = respawnLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
