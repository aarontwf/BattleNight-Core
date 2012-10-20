package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Team;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class NameplateListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public NameplateListener(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameplate(PlayerReceiveNameTagEvent event) {
        String tagged = event.getNamedPlayer().getName();

        if (BattleNight.getBattle().usersTeam.containsKey(tagged)) {
            ChatColor teamColour = BattleNight.getBattle().usersTeam.get(tagged).equals(Team.RED) ? ChatColor.RED : ChatColor.BLUE;
            event.setTag(teamColour + tagged);
        } else if (BattleNight.getBattle().spectators.contains(tagged)) {
            event.setTag(ChatColor.GRAY + tagged);
        } else if (!event.isModified()) {
            event.setTag(tagged);
        }
    }
}
