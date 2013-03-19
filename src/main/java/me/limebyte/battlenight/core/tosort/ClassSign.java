package me.limebyte.battlenight.core.tosort;

import java.util.ArrayList;

import me.limebyte.battlenight.core.listeners.SignListener;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class ClassSign {
    private final Sign sign;
    private ArrayList<String> names = new ArrayList<String>();

    public ClassSign(Sign sign) {
        this.sign = sign;
        names.ensureCapacity(2);
    }

    public void add(Player player) {
        SignListener.cleanSigns(player);

        addName(player.getName());
        refresh();
    }

    public void clear() {
        sign.setLine(2, "");
        sign.setLine(3, "");
        names.clear();
        refresh();
    }

    public Sign getSign() {
        return sign;
    }

    public void remove(Player player) {
        removeName(player.getName());
        refresh();
    }

    private void addName(String name) {
        names.remove(name);
        names.add(0, name);
    }

    private String getLine2() {
        String line2 = "";
        if (names.size() >= 1 && names.get(0) != null) {
            line2 = names.get(0);
        }
        return line2;
    }

    private String getLine3() {
        String line3 = "";
        if (names.size() >= 2 && names.get(1) != null) {
            line3 = names.get(1);
        }
        return line3;
    }

    private void refresh() {
        sign.setLine(2, getLine2());
        sign.setLine(3, getLine3());
        sign.update();
    }

    private void removeName(String name) {
        names.remove(name);
    }
}
