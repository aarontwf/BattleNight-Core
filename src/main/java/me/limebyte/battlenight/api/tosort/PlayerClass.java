package me.limebyte.battlenight.api.tosort;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public interface PlayerClass {

    public String getName();

    public Permission getPermission();

    public List<ItemStack> getItems();

    public List<ItemStack> getArmour();

    public HashMap<String, Boolean> getPermissions();

    public void equip(Player player);

}
