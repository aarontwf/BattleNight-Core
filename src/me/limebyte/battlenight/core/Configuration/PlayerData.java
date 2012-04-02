package me.limebyte.battlenight.core.Configuration;

import org.bukkit.entity.Player;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toBinaryString;

/**
 * @author LimeByte.
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
public class PlayerData {

    public void save(Player p) {

    }

    public void restore(Player p) {

    }

    protected void addKill(Player p) {

    }

    protected void addDeath(Player p) {

    }

    public int getKills(Player p) {
        return 0;
    }

    public int getDeaths(Player p) {
        return 0;
    }

    public double getKDRatio(Player p) {
        return getKills(p) / getDeaths(p);
    }

    // Obfuscate kills/deaths
    private String obfuscate(int i) {
        return toBinaryString(i).replace("0", "A").replace("1", "B");
    }

    // De-Obfuscate kills/deaths
    private int deObfuscate(String i) {
        return parseInt(i.replace("A", "0").replace("B", "1"), 2);
    }

}