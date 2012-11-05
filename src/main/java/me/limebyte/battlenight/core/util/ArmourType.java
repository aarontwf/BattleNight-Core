package me.limebyte.battlenight.core.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArmourType {
    HELMET(Material.CHAINMAIL_HELMET,
            Material.IRON_HELMET,
            Material.GOLD_HELMET,
            Material.DIAMOND_HELMET),
    CHESTPLATE(Material.CHAINMAIL_CHESTPLATE,
            Material.IRON_CHESTPLATE,
            Material.GOLD_CHESTPLATE,
            Material.DIAMOND_CHESTPLATE),
    LEGGINGS(Material.CHAINMAIL_LEGGINGS,
            Material.IRON_LEGGINGS,
            Material.GOLD_LEGGINGS,
            Material.DIAMOND_LEGGINGS),
    BOOTS(Material.CHAINMAIL_BOOTS,
            Material.IRON_BOOTS,
            Material.GOLD_BOOTS,
            Material.DIAMOND_BOOTS);

    private Material[] materials;

    ArmourType(Material... materials) {
        this.materials = materials;
    }

    public boolean contains(ItemStack stack) {
        for (Material material : materials) {
            if (stack.getType().equals(material)) return true;
        }
        return false;
    }
}
