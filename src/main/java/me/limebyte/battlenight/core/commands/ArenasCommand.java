package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.tosort.Arena;
import me.limebyte.battlenight.api.tosort.BattleNightCommand;
import me.limebyte.battlenight.api.tosort.ListPage;
import me.limebyte.battlenight.api.tosort.Waypoint;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenasCommand extends BattleNightCommand {

    public ArenasCommand() {
        super("Arenas");

        setLabel("arenas");
        setDescription("Various arena related commands.");
        setUsage("/bn arenas <action> [arena]");
        setPermission(CommandPermission.ADMIN);
    }

    private String getArenaName(Arena arena) {
        ChatColor colour = arena.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        return colour + arena.getDisplayName() + " (" + arena.getName() + ")";
    }

    private String numSetup(List<Arena> arenas) {
        int num = 0;
        int setup = 0;

        for (Arena a : arenas) {
            if (a == null) {
                continue;
            }
            num++;
            if (a.isSetup(2)) {
                setup++;
            }
        }

        return setup + "/" + num;
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        ArenaManager manager = api.getArenaManager();
        List<Arena> arenas = manager.getArenas();

        if (args.length < 1) {
            sendArenasList(sender, arenas);
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sendArenasList(sender, arenas);
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    Messenger.tell(sender, Message.ARENA_EXISTS);
                    return false;
                }
            }
            manager.register(new Arena(args[1]));
            Messenger.tell(sender, Message.ARENA_CREATED, args[1]);

            return false;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            Iterator<Arena> it = arenas.iterator();
            while (it.hasNext()) {
                Arena arena = it.next();
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    it.remove();
                    Messenger.tell(sender, Message.ARENA_DELETED, args[1]);
                    return true;
                }
            }

            return false;
        }

        if (args[0].equalsIgnoreCase("addspawn")) {
            if (!(sender instanceof Player)) {
                Messenger.tell(sender, Message.PLAYER_ONLY);
                return false;
            }

            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            Arena arena = null;
            for (Arena a : arenas) {
                if (a.getName().equalsIgnoreCase(args[1])) {
                    arena = a;
                }
            }

            if (arena == null) {
                Messenger.tell(sender, Message.INVALID_ARENA);
                return false;
            }

            Waypoint point = new Waypoint();
            point.setLocation(((Player) sender).getLocation());
            int index = arena.addSpawnPoint(point);
            Messenger.tell(sender, Message.SPAWN_CREATED, index + 1, arena);
            return true;
        }

        if (args[0].equalsIgnoreCase("removespawn")) {
            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            } else if (args.length < 3) {
                Messenger.tell(sender, "Invalid Spawn Point.");
                return false;
            }

            Arena arena = null;
            for (Arena a : arenas) {
                if (a.getName().equalsIgnoreCase(args[1])) {
                    arena = a;
                }
            }

            if (arena == null) {
                Messenger.tell(sender, Message.INVALID_ARENA);
                return false;
            }

            int index = Integer.parseInt(args[2]);
            ArrayList<Waypoint> spawns = arena.getSpawnPoints();
            if (spawns.size() < index + 1) {
                Messenger.tell(sender, "Invalid Spawn Point.");
                return false;
            }

            spawns.remove(index);
            Messenger.tell(sender, Message.SPAWN_REMOVED, index, arena);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.enable();
                    Messenger.tell(sender, Message.ARENA_ENABLED, arena);
                    return true;
                }
            }
            Messenger.tell(sender, Message.INVALID_ARENA);
            return false;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length < 2) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.disable();
                    Messenger.tell(sender, Message.ARENA_DISABLED, arena);
                    return true;
                }
            }
            Messenger.tell(sender, Message.INVALID_ARENA);
            return false;
        }

        if (args[0].equalsIgnoreCase("name")) {
            if (args.length < 3) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    String name = createString(args, 2);
                    arena.setDisplayName(name);
                    Messenger.tell(sender, Message.ARENA_NAMED, name, arena.getName());
                    return true;
                }
            }
            return false;
        }

        if (args[0].equalsIgnoreCase("texturepack")) {
            if (args.length < 3) {
                Messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.setTexturePack(args[2]);
                    Messenger.tell(sender, Message.TEXTUREPACK_SET, arena.getName());
                    return true;
                }
            }
            return false;
        }

        Messenger.tell(sender, Message.INVALID_COMMAND);
        Messenger.tell(sender, Message.USAGE, getUsage());
        return false;
    }

    private void sendArenasList(CommandSender sender, List<Arena> arenas) {
        List<String> lines = new ArrayList<String>();

        lines.add(ChatColor.WHITE + "Setup Arenas: " + numSetup(arenas));
        for (Arena arena : arenas) {
            lines.add(getArenaName(arena) + ChatColor.WHITE + " (" + arena.getSpawnPoints().size() + " Spawns)");
        }

        Messenger.tell(sender, new ListPage("BattleNight Arenas", lines));
    }

}
