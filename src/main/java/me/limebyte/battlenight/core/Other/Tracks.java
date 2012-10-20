package me.limebyte.battlenight.core.Other;

import org.bukkit.ChatColor;

public class Tracks {
    public enum Track {
        RED_WON(ChatColor.RED + "Red Team won the Battle!"), BLUE_WON(
                ChatColor.BLUE + "Blue Team won the Battle!"), DRAW(
                ChatColor.DARK_PURPLE + "Draw!"),

        BATTLE_IN_PROGRESS("A game is already in progress."), BATTLE_NOT_IN_PROGRESS(
                "A Battle is not currently in progress."), MUST_HAVE_EMPTY(
                "You must have an empty inventory to join the Battle."), CONFIG_UNSET(
                "The config file has not been set up!"), NO_PERMISSION(
                "You do not have permission to use this command."), INVALID_COMAND(
                "Invalid Command.  Type '/bn help' for help."), SPECIFY_PLAYER(
                "Please specify a player."), ALREADY_IN_TEAM(
                "You have already joined a team!"), NOT_IN_TEAM(
                "You are not in a team."), NO_TP(
                "You a currently in a Battle and are not allowed to teleport.  To leave the Battle use '/bn leave'."), NO_CHEATING(
                "Not so fast! No Cheating!"),

        WAYPOINTS_UNSET("All waypoints must be set up first."), RED_LOUNGE_SET(
                "Red Lounge Waypoint Set."), RED_SPAWN_SET(
                "Red Spawn Waypoint Set."), BLUE_LOUNGE_SET(
                "Blue Lounge Waypoint Set."), BLUE_SPAWN_SET(
                "Blue Spawn Waypoint Set."), SPECTATOR_SET(
                "Spectator Waypoint Set."), EXIT_SET("Exit Waypoint Set."),

        WELCOME_SPECTATOR("Welcome to the Spectator Area!"), WELCOME_SPECTATOR_DEATH(
                "You have been taken to the Spectator Area to watch the rest of the Battle.  To leave, type '/bn leave'."), GOODBYE_SPECTATOR(
                "You have left the Spectator Area.");

        private Track(String message) {
            msg = message;
        }

        public final String msg;

        @Override
        public String toString() {
            return msg;
        }
    }
}
