package me.limebyte.battlenight.core.old;

import me.limebyte.battlenight.api.util.PlayerData;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.hooks.Nameplates;
import me.limebyte.battlenight.core.util.SafeTeleporter;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Util {

    public static boolean inventoryEmpty(PlayerInventory inv) {
        if (inv == null) return false;

        for (ItemStack item : inv.getContents()) {
            if (item != null) return false;
        }

        for (ItemStack item : inv.getArmorContents()) {
            if (item.getType() != Material.AIR) return false;
        }

        return true;
    }

    public static boolean isSetup() {
        for (Waypoint wp : Waypoint.values()) {
            if (!wp.isSet()) return false;
        }
        return true;
    }

    public static int numSetupPoints() {
        int set = 0;
        for (Waypoint wp : Waypoint.values()) {
            if (wp.isSet()) {
                set++;
            }
        }
        return set;
    }

    public static void teleportAllToSpawn() {
        for (String name : BattleNight.getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                if (BattleNight.getBattle().usersTeam.get(name).equals(Team.RED)) {
                    SafeTeleporter.tp(currentPlayer, Waypoint.RED_SPAWN);
                }
                if (BattleNight.getBattle().usersTeam.get(name).equals(Team.BLUE)) {
                    SafeTeleporter.tp(currentPlayer, Waypoint.BLUE_SPAWN);
                }
            }
        }

        SafeTeleporter.startTeleporting();
    }

    public static boolean preparePlayer(Player player) {
        String invType = ConfigManager.get(Config.MAIN).getString("InventoryType", "save");

        if (invType.equalsIgnoreCase("prompt")) {
            if (!inventoryEmpty(player.getInventory())) return false;
        }

        PlayerData.store(player);
        PlayerData.reset(player);
        return true;
    }

    public static void setNames(Player player) {
        String name = player.getName();
        String pListName = ChatColor.GRAY + "[BN] " + name;
        player.setPlayerListName(pListName.length() < 16 ? pListName : pListName.substring(0, 16));
        Nameplates.refresh(player);
    }

}
