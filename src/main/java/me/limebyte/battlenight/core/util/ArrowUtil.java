package me.limebyte.battlenight.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ArrowUtil {

    private static final ItemStack prev = createArrow("Previous Player");
    private static final ItemStack next = createArrow("Next Player");

    public static void equipArrows(Player player) {
        player.getInventory().setItem(0, prev);
        player.getInventory().setItem(8, next);
    }

    private static ItemStack createArrow(String action) {
        ItemStack stack = new ItemStack(Material.PAPER);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + action);
        stack.setItemMeta(meta);
        return stack;
    }
}
