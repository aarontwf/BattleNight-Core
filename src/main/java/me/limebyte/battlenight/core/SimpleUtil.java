package me.limebyte.battlenight.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SimpleUtil {

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
