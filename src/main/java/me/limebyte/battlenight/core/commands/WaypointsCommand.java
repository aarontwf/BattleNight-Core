package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Waypoint;
import me.limebyte.battlenight.core.chat.ListPage;

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

        ListPage page = new ListPage("BattleNight Waypoints", lines);
        sender.sendMessage(page.getPage());
        return true;
    }

    private static ChatColor getWaypointColour(Waypoint waypoint) {
        return BattleNight.pointSet(waypoint) ? ChatColor.GREEN : ChatColor.RED;
    }

}
