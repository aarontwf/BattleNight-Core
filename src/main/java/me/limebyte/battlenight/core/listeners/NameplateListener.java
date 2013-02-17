package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Team;
import me.limebyte.battlenight.api.battle.TeamedBattle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class NameplateListener extends APIRelatedListener {

    public NameplateListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameplate(PlayerReceiveNameTagEvent event) {
        Player player = event.getNamedPlayer();
        String tag = ChatColor.stripColor(event.getTag());
        Battle battle = getAPI().getBattle();

        if (battle.containsPlayer(player)) {
            ChatColor teamColour = ChatColor.RED;
            if (battle instanceof TeamedBattle) {
                Team team = ((TeamedBattle) battle).getTeam(player);
                if (team != null) teamColour = team.getColour();
            }
            event.setTag(teamColour + tag);
        } else if (battle.containsSpectator(player)) {
            event.setTag(ChatColor.DARK_GRAY + tag);
        } else if (!event.isModified()) {
            event.setTag(tag);
        }
    }
}
