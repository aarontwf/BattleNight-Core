package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.core.tosort.Waypoint;
import me.limebyte.battlenight.core.util.ListPage;

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

    private static ChatColor getWaypointColour(Waypoint waypoint) {
        return waypoint.isSet() ? ChatColor.GREEN : ChatColor.RED;
    }

    private String numSetup(Waypoint... waypoints) {
        int num = 0;
        int setup = 0;

        for (Waypoint wp : waypoints) {
            if (wp == null) {
                continue;
            }
            num++;
            if (wp.isSet()) {
                setup++;
            }
        }

        return setup + "/" + num;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        List<String> lines = new ArrayList<String>();
        ArenaManager manager = api.getArenaManager();
        Waypoint w1 = manager.getLounge();
        Waypoint w2 = manager.getExit();

        lines.add(ChatColor.WHITE + "Setup points: " + numSetup(w1, w2));
        lines.add(getWaypointColour(w1) + "Lounge" + ChatColor.WHITE + " (/bn set lounge...)");
        lines.add(getWaypointColour(w2) + "Exit" + ChatColor.WHITE + " (/bn set exit...)");

        api.getMessenger().tell(sender, new ListPage("BattleNight Waypoints", lines));
        return true;
    }

}
