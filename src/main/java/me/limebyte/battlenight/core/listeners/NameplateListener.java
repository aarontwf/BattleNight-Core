package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.core.Team;
import me.limebyte.battlenight.core.TeamedBattle;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class NameplateListener extends APIRelatedListener {

    private static final String SPECTATOR_TAG = ChatColor.DARK_GRAY + "[Spectator]";

    public NameplateListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerReceiveNameTag(PlayerReceiveNameTagEvent event) {
        Player player = event.getNamedPlayer();
        String tag = player.getName();
        Battle battle = getAPI().getBattle();

        if (battle.containsPlayer(player)) {
            ChatColor teamColour = ChatColor.RED;
            if (battle instanceof TeamedBattle) {
                Team team = ((TeamedBattle) battle).getTeam(player);
                if (team != null) {
                    teamColour = team.getColour();
                }
            }
            event.setTag(teamColour + tag);
        } else if (getAPI().getSpectatorManager().getSpectators().contains(player.getName())) {
            event.setTag(SPECTATOR_TAG);
        } else if (!event.isModified()) {
            event.setTag(tag);
        }
    }
}
