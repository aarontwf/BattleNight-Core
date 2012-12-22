package me.limebyte.battlenight.core;

import me.limebyte.battlenight.api.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;
import org.kitteh.tag.TagAPI;

public class SimpleUtil implements Util {

    private static final String LOC_SEP = ", ";

    public void preparePlayer(Player player, Location dest) {
        PlayerData.store(player);
        player.teleport(dest, TeleportCause.PLUGIN);
        reset(player);
    }

    public void restorePlayer(Player player) {
        PlayerData.restore(player, false);
    }

    public String parseLocation(Location loc) {
        String w = loc.getWorld().getName();
        double x = loc.getBlockX() + 0.5;
        double y = loc.getBlockY();
        double z = loc.getBlockZ() + 0.5;
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return w + "(" + x + LOC_SEP + y + LOC_SEP + z + LOC_SEP + yaw + LOC_SEP + pitch + ")";
    }

    public Location parseLocation(String string) {
        String[] parts = string.split("\\(");
        World w = Bukkit.getServer().getWorld(parts[0]);

        String[] coords = parts[1].substring(0, parts[1].length() - 1).split(LOC_SEP);
        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);
        double z = Double.parseDouble(coords[2]);
        float yaw = Float.parseFloat(coords[3]);
        float pitch = Float.parseFloat(coords[4]);

        return new Location(w, x, y, z, yaw, pitch);
    }

    public static void reset(Player player) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!player.canSee(p)) {
                player.showPlayer(p);
            }
        }

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.addPotionEffect(new PotionEffect(effect.getType(), 0, 0), true);
        }

        player.setFlying(false);
        player.setAllowFlight(false);
        player.getEnderChest().clear();
        player.setExhaustion(0);
        player.setExp(0);
        player.setFallDistance(0);
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getMaxHealth());
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
        player.setLevel(0);
        String pListName = ChatColor.GRAY + "[BN] " + player.getName();
        player.setPlayerListName(pListName.length() < 16 ? pListName : pListName.substring(0, 16));
        player.resetPlayerTime();
        player.setRemainingAir(player.getMaximumAir());
        player.setSaturation(20);
        player.setTicksLived(1);
        player.setVelocity(new Vector());
        player.setWalkSpeed(0.2F);
        player.setSleepingIgnored(true);
        player.setSneaking(false);
        player.setSprinting(false);
        TagAPI.refreshPlayer(player);
    }

    @Override
    public boolean inventoryEmpty(PlayerInventory inv) {
        if (inv == null) return false;

        for (ItemStack item : inv.getContents()) {
            if (item != null) return false;
        }

        for (ItemStack item : inv.getArmorContents()) {
            if (item.getType() != Material.AIR) return false;
        }

        return true;
    }

}
