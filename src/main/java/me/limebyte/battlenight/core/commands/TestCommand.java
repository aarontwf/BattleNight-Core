package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends BattleNightCommand {

    protected TestCommand() {
        super("Test");

        setLabel("test");
        setDescription("Runs the specified test.");
        setUsage("/bn test <test>");
        setPermission(CommandPermission.ADMIN);
        setAliases(Arrays.asList("try"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        // Commands with 1 argument
        if (args.length < 1) {
            Messenger.tell(sender, Message.SPECIFY_TEST);
            Messenger.tell(sender, Message.USAGE, getUsage());
            return false;
        }

        if (args[0].equalsIgnoreCase("join")) {
            if (isNotPlayer(sender)) return false;
            BattleNight.instance.getAPI().getBattle().addPlayer((Player) sender);
            return true;
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (isNotPlayer(sender)) return false;
            BattleNight.instance.getAPI().getBattle().removePlayer((Player) sender);
            return true;
        }

        // Commands with 2 arguments
        if (args.length < 2) {
            Messenger.tell(sender, Message.INCORRECT_USAGE);
            return false;
        }

        if (args[0].equalsIgnoreCase("create")) {
            BattleNightAPI api = BattleNight.instance.getAPI();
            for (Arena arena : api.getArenas()) {
                if (arena.getName().equals(args[1])) {
                    Messenger.tell(sender, "An Arena by that name already exists!");
                    return false;
                }
            }
            api.registerArena(new Arena(args[1]));
            Messenger.tell(sender, "Arena " + args[1] + " created.");
            return true;
        } else if (args[0].equalsIgnoreCase("addspawn")) {
            if (isNotPlayer(sender)) return false;
            BattleNightAPI api = BattleNight.instance.getAPI();
            Arena arena = null;

            for (Arena a : api.getArenas()) {
                if (a.getName().equals(args[1])) {
                    arena = a;
                }
            }

            if (arena == null) {
                Messenger.tell(sender, "An Arena by that name does not exist!");
                return false;
            }
            Waypoint point = new Waypoint();
            point.setLocation(((Player) sender).getLocation());
            arena.addSpawnPoint(point);
            Messenger.tell(sender, "Spawn point created.");
            return true;
        }

        return false;
    }

    private boolean isNotPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            Messenger.tell(sender, Message.PLAYER_ONLY);
            return true;
        }
        return false;
    }
}
