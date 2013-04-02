package me.limebyte.battlenight.api.util;

import org.bukkit.ChatColor;

public enum Message {
    // Battle Messages
    BATTLE_IN_PROGRESS(ChatColor.RED + "A Battle is already in progress!"),
    BATTLE_NOT_IN_PROGRESS(ChatColor.RED + "A Battle is not in progress!"),
    ALREADY_IN_BATTLE(ChatColor.RED + "You have already joined a Battle!"),
    NOT_IN_BATTLE(ChatColor.RED + "You are not in a Battle!"),
    NO_CHEATING(ChatColor.RED + "Not so fast!  No cheating."),
    NO_TELEPORTING(ChatColor.RED + "You are not permitted to teleport while in a Battle."),
    NO_COMMAND(ChatColor.RED + "You are not permitted to perform this command while in a Battle."),
    ALREADY_SPECTATING(ChatColor.RED + "You are already watching a Battle!"),
    WELCOME_SPECTATOR("Welcome!  You are spectating $1."),
    TARGET_CYCLED("You are now spectating $1."),
    GOODBYE_SPECTATOR("You have stopped spectating."),
    CANT_SPECTATE("You must leave the Battle before spectating."),
    WAYPOINTS_UNSET(ChatColor.RED + "All waypoints must be set up first."),
    NO_ARENAS(ChatColor.RED + "No setup or enabled Arenas!"),
    JOINED_BATTLE("Welcome!  You are playing on $1."),
    PLAYER_JOINED_BATTLE("$1" + ChatColor.GRAY + " has joined the Battle."),
    BATTLE_STARTED(ChatColor.GREEN + "Let the Battle begin!"),
    BATTLE_ENDED("The Battle has ended."),
    BATTLE_FULL(ChatColor.RED + "The battle is full!"),
    NOT_ENOUGH_PLAYERS(ChatColor.RED + "You need $1 more players to start the battle!"),

    // Teamed Battle Messages
    TEAM_IS_READY("$1" + ChatColor.WHITE + " Team is ready!"),
    JOINED_TEAM("You are on team $1" + ChatColor.WHITE + "."),
    PLAYER_JOINED_TEAM("$1" + ChatColor.GRAY + " has joined team $2" + ChatColor.GRAY + "."),

    // Lounge Messages
    NO_PERMISSION_CLASS(ChatColor.RED + "You do not have permission to use this class."),
    NO_CLASS(ChatColor.RED + "You have not selected a class!"),
    PLAYER_IS_READY("$1" + ChatColor.GRAY + " is ready!"),

    // Kill Messages
    YOU_WERE_KILLED(ChatColor.GRAY + "You were killed by $1" + ChatColor.GRAY + "!"),
    PLAYER_WAS_KILLED("$1 " + ChatColor.GRAY + "was killed by" + ChatColor.WHITE + " $2" + ChatColor.GRAY + "."),

    // Win Messages
    PLAYER_WON(ChatColor.GOLD + "$1 won the Battle!"),
    TEAM_WON("$1 Team won the Battle!"),
    DRAW(ChatColor.DARK_PURPLE + "Draw!"),

    // Class Sign Creation Messages
    SUCCESSFUL_SIGN(ChatColor.GREEN + "Successfully created sign for $1!"),
    UNSUCCESSFUL_SIGN(ChatColor.RED + "Error creating sign for $1!  Leave last 3 lines blank."),

    // Command Messages
    USAGE("Usage: $1"),
    NO_PERMISSION_COMMAND(ChatColor.RED + "You do not have permission to use this $1."),
    PLAYER_ONLY(ChatColor.RED + "This command can only be performed by a player!"),
    SPECIFY_MESSAGE(ChatColor.RED + "Please specify a message."),
    SPECIFY_PLAYER(ChatColor.RED + "Please specify a player."),
    SPECIFY_WAYPOINT(ChatColor.RED + "Please specify a waypoint."),
    SPECIFY_COORDINATE(ChatColor.RED + "Please specify a coordinate."),
    SPECIFY_ARENA(ChatColor.RED + "Please specify an arena name."),
    SPECIFY_TEST(ChatColor.RED + "Please specify a test."),
    INVALID_COMMAND(ChatColor.RED + "Invalid Command.  Type '/bn help' for help."),
    INVALID_WAYPOINT(ChatColor.RED + "Invalid Waypoint.  Type \"/bn waypoints\" for a list."),
    WAYPOINT_SET(ChatColor.GREEN + "$1 Waypoint set to $2 in world $3."),
    WAYPOINT_SET_CURRENT_LOC(ChatColor.GREEN + "$1 Waypoint set to your current location."),
    WAYPOINT_SET_THIS_WORLD(ChatColor.GREEN + "$1 Waypoint set to $2 in this world."),
    WAYPOINT_UNSET(ChatColor.RED + "$1 Waypoint is not set."),
    KICKED("You have been kicked from the Battle."),
    PLAYER_KICKED("$1 has been kicked from the Battle."),
    REASONED_KICK("You have been kicked from the Battle for $1."),
    PLAYER_REASONED_KICKED("$1 has been kicked from the Battle for $2."),

    INVALID_ARENA(ChatColor.RED + "An Arena by that name does not exists!"),
    ARENA_EXISTS(ChatColor.RED + "An Arena by that name already exists!"),
    ARENA_CREATED(ChatColor.GREEN + "Arena $1 created."),
    ARENA_DELETED(ChatColor.GREEN + "Arena $1 deleted."),
    ARENA_ENABLED(ChatColor.GREEN + "$1 Arena enabled."),
    ARENA_DISABLED(ChatColor.GREEN + "$1 Arena disabled."),
    SPAWN_CREATED(ChatColor.GREEN + "Spawn point $1 created for Arena $2."),
    SPAWN_REMOVED(ChatColor.GREEN + "Spawn point $1 removed for Arena $2."),
    ARENA_NAMED(ChatColor.GREEN + "Set display name to $1 for Arena $2."),
    TEXTUREPACK_SET(ChatColor.GREEN + "Texture pack set for Arena $1."),

    INCORRECT_USAGE(ChatColor.RED + "Incorrect usage."),
    DEPRICATED_COMMAND(ChatColor.RED + "This command is deprecated, please use \"/bn $1\" instead."),
    CANT_FIND_PLAYER(ChatColor.RED + "Can't find player \"$1\"."),
    CANT_FIND_WORLD(ChatColor.RED + "Can't find world \"$1\"."),
    RELOADING("Reloading BattleNight..."),
    RELOAD_SUCCESSFUL(ChatColor.GREEN + "Reloaded successfully."),
    RELOAD_FAILED(ChatColor.RED + "Reload failed.  See console for error log."),
    PLAYER_NOT_IN_BATTLE(ChatColor.RED + "Player \"$1\" is not in the Battle.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
