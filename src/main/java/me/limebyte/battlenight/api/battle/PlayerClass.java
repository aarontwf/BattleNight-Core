package me.limebyte.battlenight.api.battle;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public interface PlayerClass {

    public String getName();

    public Permission getPermission();

    public List<ItemStack> getItems();

    public List<ItemStack> getArmour();

    public void equip(Player player);

}
