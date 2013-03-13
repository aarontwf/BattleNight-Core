package me.limebyte.battlenight.api.util;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public interface PlayerClass {

    public void equip(Player player);

    public List<ItemStack> getArmour();

    public List<ItemStack> getItems();

    public String getName();

    public Permission getPermission();

    public HashMap<String, Boolean> getPermissions();

}
