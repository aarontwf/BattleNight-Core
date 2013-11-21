package me.limebyte.battlenight.core.listeners;

import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;

public class LobbyListener extends APIRelatedListener {

    private static final List<DamageCause> DAMAGE = Arrays.asList(new DamageCause[] { DamageCause.SUFFOCATION, DamageCause.VOID });

    public LobbyListener(BattleNightAPI api) {
        super(api);
    }

    private boolean isInLobby(PlayerEvent event) {
        return isInLobby(event.getPlayer());
    }

    private boolean isInLobby(Player player) {
        if (player == null) return false;
        return getAPI().getLobby().contains(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isInLobby(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (isInLobby(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isInLobby(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (isInLobby((Player) event.getEntity())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (DAMAGE.contains(event.getCause())) return;
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (isInLobby((Player) entity)) event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (isInLobby(player)) getAPI().getLobby().removePlayer(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Entity shooter = event.getEntity().getShooter();
        if (shooter instanceof Player) {
            if (isInLobby((Player) shooter)) event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        Entity remover = event.getRemover();
        if (remover instanceof Player) {
            if (isInLobby((Player) remover)) event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (isInLobby(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity clicker = event.getWhoClicked();
        if (clicker instanceof Player) {
            if (!isInLobby((Player) clicker)) return;
            if (event.getCursor() == null) return;
            if (event.getSlotType() != SlotType.OUTSIDE || event.getSlotType() != SlotType.CONTAINER) return;
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (isInLobby(event)) event.setAmount(0);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemCosume(PlayerItemConsumeEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (isInLobby(event)) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        Entity attacker = event.getAttacker();
        if (attacker instanceof Player) {
            if (isInLobby((Player) attacker)) event.setCancelled(true);
        }
    }

}
