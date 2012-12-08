package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.util.chat.ListPage;
import me.limebyte.battlenight.core.util.chat.Messaging;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WaypointsCommand extends BattleNightCommand {

    protected WaypointsCommand() {
        super("Waypoints");

        this.setLabel("waypoints");
        this.setDescription("Displays the BattleNight waypoints.");
        this.setUsage("/bn waypoints");
        this.setPermission(CommandPermission.ADMIN);
        this.setAliases(Arrays.asList("wpoints"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        List<String> lines = new ArrayList<String>();
        Waypoint[] waypoints = Waypoint.values();

        lines.add(ChatColor.WHITE + "Setup points: " + BattleNight.numSetupPoints() + "/" + waypoints.length);
        for (Waypoint wp : waypoints) {
            lines.add(getWaypointColour(wp) + wp.getDisplayName() + ChatColor.WHITE + " (/bn set " + wp.getName() + "...)");
        }

        Messaging.tell(sender, new ListPage("BattleNight Waypoints", lines));
        return true;
    }

    private static ChatColor getWaypointColour(Waypoint waypoint) {
        return waypoint.isSet() ? ChatColor.GREEN : ChatColor.RED;
    }

}
