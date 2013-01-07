package me.limebyte.battlenight.core.old;

import java.util.Map.Entry;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Metadata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Team {
    RED("Red", ChatColor.RED), BLUE("Blue", ChatColor.BLUE);

    private String name;
    private ChatColor colour;

    Team(String name, ChatColor colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isReady() {
        int members = 0;
        int membersReady = 0;

        for (Entry<String, Team> entry : BattleNight.getBattle().usersTeam.entrySet()) {
            Player player = Bukkit.getPlayerExact(entry.getKey());
            if (player != null) {
                if (entry.getValue() == this) {
                    members++;
                    if (Metadata.getBattleClass(player) != null) {
                        membersReady++;
                    }
                }
            }
        }

        return members == membersReady && members > 0;
    }
}
