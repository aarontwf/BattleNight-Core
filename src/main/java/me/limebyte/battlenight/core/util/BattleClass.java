package me.limebyte.battlenight.core.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BattleClass {
    private String name;
    private List<ItemStack> items, armour;

    private static final int LAST_INV_SLOT = 35;

    public BattleClass(String name, List<ItemStack> items, List<ItemStack> armour) {
        this.name = name;
        this.items = items;
        this.armour = armour;

        String permission = "battlenight.class." + name.toLowerCase().replaceAll(" ", "-").replaceAll(".", "");
        Permission perm = new Permission(permission, "Permission for the class: " + name + ".", PermissionDefault.TRUE);
        try {
            Bukkit.getServer().getPluginManager().addPermission(perm);
        } catch (Exception e) {
        }
    }

    public String getName() {
        return name;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public List<ItemStack> getArmour() {
        return armour;
    }

    public void equip(Player player) {
        PlayerInventory inv = player.getInventory();

        // Main Inventory
        for (int i = 0; i < items.size(); i++) {
            if (i > LAST_INV_SLOT) break;
            inv.setItem(i, items.get(i));
        }

        // Armour
        inv.setHelmet(armour.get(0));
        inv.setChestplate(armour.get(1));
        inv.setLeggings(armour.get(2));
        inv.setBoots(armour.get(3));
    }
}
