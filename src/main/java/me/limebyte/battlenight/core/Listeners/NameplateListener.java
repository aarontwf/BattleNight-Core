package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Team;

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

        if (BattleNight.BattleUsersTeam.containsKey(tagged)) {
            ChatColor teamColour = BattleNight.BattleUsersTeam.get(tagged).equals(Team.RED) ? ChatColor.RED : ChatColor.BLUE;
            event.setTag(teamColour + tagged);
        } else if (BattleNight.BattleSpectators.containsKey(tagged)) {
            event.setTag(ChatColor.GRAY + tagged);
        } else if (!event.isModified()) {
            event.setTag(tagged);
        }
    }
}
