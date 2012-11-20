package me.limebyte.battlenight.api;

import me.limebyte.battlenight.core.BattleNight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public final class BattleNightAPI {

    private static BattleNight battleNight;

    private static final void init() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BattleNight");

        if (plugin == null) throw new NoClassDefFoundError("BattleNight is not running on this server.");
        if (!(plugin instanceof BattleNight)) throw new ClassCastException("This is not the BattleNight you are looking for...");

        battleNight = (BattleNight) plugin;
    }

    public static final Util getUtil() {
        if (battleNight == null) init();
        return BattleNight.getUtil();
    }

}
