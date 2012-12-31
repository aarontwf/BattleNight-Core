package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.api.util.ListPage;
import me.limebyte.battlenight.core.old.Util;
import me.limebyte.battlenight.core.old.Waypoint;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class WaypointsCommand extends BattleNightCommand {

    protected WaypointsCommand() {
        super("Waypoints");

        setLabel("waypoints");
        setDescription("Displays the BattleNight waypoints.");
        setUsage("/bn waypoints");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("wpoints"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        List<String> lines = new ArrayList<String>();
        Waypoint[] waypoints = Waypoint.values();

        lines.add(ChatColor.WHITE + "Setup points: " + Util.numSetupPoints() + "/" + waypoints.length);
        for (Waypoint wp : waypoints) {
            lines.add(getWaypointColour(wp) + wp.getDisplayName() + ChatColor.WHITE + " (/bn set " + wp.getName() + "...)");
        }

        Messenger.tell(sender, new ListPage("BattleNight Waypoints", lines));
        return true;
    }

    private static ChatColor getWaypointColour(Waypoint waypoint) {
        return waypoint.isSet() ? ChatColor.GREEN : ChatColor.RED;
    }

}
