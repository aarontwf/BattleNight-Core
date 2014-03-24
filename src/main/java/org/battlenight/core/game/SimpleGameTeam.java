package org.battlenight.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.battlenight.api.game.GameTeam;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.common.collect.Maps;

public class SimpleGameTeam implements GameTeam {

    private static Map<String, SimpleGameTeam> BY_NAME = Maps.newHashMap();

    static {
        register(new SimpleGameTeam("Orange", ChatColor.GOLD, Color.ORANGE));
        register(new SimpleGameTeam("Magenta", ChatColor.DARK_PURPLE, Color.MAROON));
        register(new SimpleGameTeam("Aqua", ChatColor.AQUA, Color.AQUA));
        register(new SimpleGameTeam("Yellow", ChatColor.YELLOW, Color.YELLOW));
        register(new SimpleGameTeam("Green", ChatColor.GREEN, Color.LIME));
        register(new SimpleGameTeam("Pink", ChatColor.LIGHT_PURPLE, Color.PURPLE));
        register(new SimpleGameTeam("Dark Gray", ChatColor.DARK_GRAY, Color.GRAY));
        register(new SimpleGameTeam("Gray", ChatColor.GRAY, Color.SILVER));
        register(new SimpleGameTeam("Cyan", ChatColor.DARK_AQUA, Color.AQUA));
        register(new SimpleGameTeam("Blue", ChatColor.BLUE, Color.BLUE));
        register(new SimpleGameTeam("Dark Green", ChatColor.DARK_GREEN, Color.GREEN));
        register(new SimpleGameTeam("Red", ChatColor.RED, Color.RED));
        register(new SimpleGameTeam("Black", ChatColor.BLACK, Color.BLACK));
    }

    private String displayName;
    private ChatColor chatColour;
    private Color colour;
    private int players;

    private SimpleGameTeam(String displayName, ChatColor chatColour, Color colour) {
        this.displayName = displayName;
        this.chatColour = chatColour;
        this.colour = colour;
        this.players = 0;

        BY_NAME.put(toString(), this);
    }

    @Override
    public String toString() {
        return displayName.toLowerCase().replace(' ', '-');
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }

    @Override
    public String getName() {
        return toString();
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getChatColour() {
        return chatColour;
    }

    public int getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        ItemStack armour = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) armour.getItemMeta();
        meta.setColor(colour);
        armour.setItemMeta(meta);
        player.getInventory().setHelmet(armour);

        armour = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        meta = (LeatherArmorMeta) armour.getItemMeta();
        meta.setColor(colour);
        armour.setItemMeta(meta);
        player.getInventory().setChestplate(armour);

        armour = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        meta = (LeatherArmorMeta) armour.getItemMeta();
        meta.setColor(colour);
        armour.setItemMeta(meta);
        player.getInventory().setLeggings(armour);

        armour = new ItemStack(Material.LEATHER_BOOTS, 1);
        meta = (LeatherArmorMeta) armour.getItemMeta();
        meta.setColor(colour);
        armour.setItemMeta(meta);
        player.getInventory().setBoots(armour);

        players++;
    }

    public void removePlayer(Player player) {
        players--;
    }

    public void reset() {
        players = 0;
    }

    private static void register(SimpleGameTeam team) {
        BY_NAME.put(team.toString(), team);
    }

    public static SimpleGameTeam fromString(String team) {
        return BY_NAME.get(team.toLowerCase());
    }

    public static List<GameTeam> getTeams() {
        return new ArrayList<GameTeam>(BY_NAME.values());
    }

}
