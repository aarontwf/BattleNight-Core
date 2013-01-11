package me.limebyte.battlenight.api.event;

import me.limebyte.battlenight.api.battle.Battle;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class BattleRespawnEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Battle battle;
    private boolean cancel = false;

    public BattleRespawnEvent(Battle battle, Player player) {
        super(player);
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
