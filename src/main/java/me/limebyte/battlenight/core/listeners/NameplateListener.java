package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Team;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class NameplateListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameplate(PlayerReceiveNameTagEvent event) {
        String name = event.getNamedPlayer().getName();
        String tag = ChatColor.stripColor(event.getTag());

        if (BattleNight.getBattle().usersTeam.containsKey(name)) {
            ChatColor teamColour = BattleNight.getBattle().usersTeam.get(name).equals(Team.RED) ? ChatColor.RED : ChatColor.BLUE;
            event.setTag(teamColour + tag);
        } else if (BattleNight.getBattle().spectators.contains(name)) {
            event.setTag(ChatColor.GRAY + tag);
        } else if (!event.isModified()) {
            event.setTag(tag);
        }
    }
}
