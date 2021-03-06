package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.battle.Waypoint;
import me.limebyte.battlenight.api.managers.ArenaManager;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.battle.SimpleArena;
import me.limebyte.battlenight.core.util.SimpleWaypoint;
import me.limebyte.battlenight.core.util.chat.ListPage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class ArenasCommand extends BattleNightCommand {

    public ArenasCommand() {
        super("Arenas");

        setLabel("arenas");
        setDescription("Various arena related commands.");
        setUsage("/bn arenas <action> [arena]");
        setPermission(CommandPermission.ADMIN);
        setPrimaryChoices(ImmutableList.of("list", "create", "delete", "addspawn", "removespawn", "enable", "disable", "setdisplay", "settexturepack"));
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

    private void sendArenasList(CommandSender sender, List<Arena> arenas) {
        List<String> lines = new ArrayList<String>();

        lines.add(ChatColor.WHITE + "Setup Arenas: " + numSetup(arenas));
        for (Arena arena : arenas) {
            lines.add(getArenaName(arena) + ChatColor.WHITE + " (" + arena.getSpawnPoints().size() + " Spawns)");
        }

        api.getMessenger().tell(sender, new ListPage("BattleNight Arenas", lines));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        ArenaManager manager = api.getArenaManager();
        Messenger messenger = api.getMessenger();
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
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    messenger.tell(sender, Message.ARENA_EXISTS);
                    return false;
                }
            }
            manager.register(new SimpleArena(args[1]));
            messenger.tell(sender, Message.ARENA_CREATED, args[1]);

            return false;
        }

        if (args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            Iterator<Arena> it = arenas.iterator();
            while (it.hasNext()) {
                Arena arena = it.next();
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    it.remove();
                    messenger.tell(sender, Message.ARENA_DELETED, args[1]);
                    return true;
                }
            }

            return false;
        }

        if (args[0].equalsIgnoreCase("addspawn")) {
            if (!(sender instanceof Player)) {
                messenger.tell(sender, messenger.get("command.player-only"));
                return false;
            }

            if (args.length < 2) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            Arena arena = null;
            for (Arena a : arenas) {
                if (a.getName().equalsIgnoreCase(args[1])) {
                    arena = a;
                }
            }

            if (arena == null) {
                messenger.tell(sender, Message.INVALID_ARENA);
                return false;
            }

            Waypoint point = new SimpleWaypoint("Spawn");
            point.setLocation(((Player) sender).getLocation());
            int index = arena.addSpawnPoint(point);
            messenger.tell(sender, Message.SPAWN_CREATED, index + 1, arena);
            return true;
        }

        if (args[0].equalsIgnoreCase("removespawn")) {
            if (args.length < 2) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            } else if (args.length < 3) {
                messenger.tell(sender, "Invalid Spawn Point.");
                return false;
            }

            Arena arena = null;
            for (Arena a : arenas) {
                if (a.getName().equalsIgnoreCase(args[1])) {
                    arena = a;
                }
            }

            if (arena == null) {
                messenger.tell(sender, Message.INVALID_ARENA);
                return false;
            }

            int index = Integer.parseInt(args[2]);
            ArrayList<Waypoint> spawns = arena.getSpawnPoints();
            if (spawns.size() < index + 1) {
                messenger.tell(sender, "Invalid Spawn Point.");
                return false;
            }

            spawns.remove(index);
            messenger.tell(sender, Message.SPAWN_REMOVED, index, arena);
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (args.length < 2) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.enable();
                    messenger.tell(sender, Message.ARENA_ENABLED, arena);
                    return true;
                }
            }
            messenger.tell(sender, Message.INVALID_ARENA);
            return false;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (args.length < 2) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.disable();
                    messenger.tell(sender, Message.ARENA_DISABLED, arena);
                    return true;
                }
            }
            messenger.tell(sender, Message.INVALID_ARENA);
            return false;
        }

        if (args[0].equalsIgnoreCase("setdisplay")) {
            if (args.length < 3) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    String name = createString(args, 2);
                    arena.setDisplayName(name);
                    messenger.tell(sender, Message.ARENA_NAMED, name, arena.getName());
                    return true;
                }
            }
            return false;
        }

        if (args[0].equalsIgnoreCase("settexturepack")) {
            if (args.length < 3) {
                messenger.tell(sender, Message.SPECIFY_ARENA);
                return false;
            }

            for (Arena arena : arenas) {
                if (arena.getName().equalsIgnoreCase(args[1])) {
                    arena.setTexturePack(args[2]);
                    messenger.tell(sender, Message.TEXTUREPACK_SET, arena.getName());
                    return true;
                }
            }
            return false;
        }

        messenger.tell(sender, Message.INVALID_COMMAND);
        messenger.tell(sender, messenger.get("command.usage"), getUsage());
        return false;
    }

}
