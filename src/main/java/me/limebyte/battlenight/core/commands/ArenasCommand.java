package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.util.BattleNightCommand;
import me.limebyte.battlenight.api.util.ListPage;
import me.limebyte.battlenight.core.util.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ArenasCommand extends BattleNightCommand {

    public ArenasCommand() {
        super("Arenas");

        setLabel("arenas");
        setDescription("Displays the BattleNight arenas.");
        setUsage("/bn arenas");
        setPermission(CommandPermission.ADMIN);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        List<String> lines = new ArrayList<String>();
        List<Arena> arenas = api.getArenas();

        lines.add(ChatColor.WHITE + Integer.toString(arenas.size()) + " Arenas");
        for (Arena arena : arenas) {
            lines.add(getArenaName(arena) + ChatColor.WHITE + " (" + arena.getSpawnPoints().size() + " Spawns)");
        }

        Messenger.tell(sender, new ListPage("BattleNight Arenas", lines));
        return true;
    }

    private String getArenaName(Arena arena) {
        ChatColor colour = arena.isEnabled() ? ChatColor.GREEN : ChatColor.RED;
        return colour + arena.getDisplayName();
    }

}
