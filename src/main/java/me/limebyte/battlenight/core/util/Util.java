package me.limebyte.battlenight.core.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

public class Util {

    private static String toEnum(String name) {
        return name.toUpperCase().replace('-', '_');
    }

    private static String toName(String name) {
        return name.toLowerCase().replace('_', '-');
    }

    public static Material getMaterial(String name) {
        return Material.getMaterial(toEnum(name));
    }

    public static Enchantment getEnchantment(String name) {
        return Enchantment.getByName(toEnum(name));
    }

    public static PotionEffectType getPotionEffect(String name) {
        return PotionEffectType.getByName(toEnum(name));
    }

    public static String getName(Material material) {
        return toName(material.toString());
    }

    public static String getName(Enchantment enchantment) {
        return toName(enchantment.getName());
    }

    public static String getName(PotionEffectType potionEffect) {
        return toName(potionEffect.getName());
    }

}
