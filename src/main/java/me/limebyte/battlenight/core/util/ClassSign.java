package me.limebyte.battlenight.core.util;

import me.limebyte.battlenight.core.listeners.SignListener;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ClassSign {
    private final Sign sign;
    private String name1 = "";
    private String name2 = "";

    public ClassSign(Sign sign) {
        this.sign = sign;
    }

    public void add(Player player) {
        SignListener.cleanSigns(player);

        // Third line is not empty
        if (!this.sign.getLine(2).isEmpty()) {
            String line2 = this.sign.getLine(2);

            // Move the first name down
            this.sign.setLine(3, line2);
            this.setName2(line2);
        }

        // Add the players name
        String name = player.getName();
        this.sign.setLine(2, name);
        this.setName1(name);

        // Update the sign
        this.sign.update();
    }

    public void remove(Player player) {
        String name = player.getName();

        // Forth line has the players name
        if (this.getName2() == name) {
            // Clear line four
            this.sign.setLine(3, "");
            this.setName2("");

            // Update the sign
            this.sign.update();
        }

        // Third line has the players name
        if (this.getName1() == name) {
            // Move the second name up
            this.sign.setLine(2, sign.getLine(3));
            this.sign.setLine(3, "");
            this.setName1(this.getName2());
            this.setName2("");

            // Update the sign
            this.sign.update();
        }
    }

    public void clear() {
        this.sign.setLine(2, "");
        this.sign.setLine(3, "");
        this.setName1("");
        this.setName2("");

        // Update the sign
        this.sign.update();
    }

    public Sign getSign() {
        return this.sign;
    }

    public String getName1() {
        return this.name1;
    }

    public String getName2() {
        return this.name2;
    }

    public void setName1(String name) {
        this.name1 = name;
    }

    public void setName2(String name) {
        this.name2 = name;
    }
}
