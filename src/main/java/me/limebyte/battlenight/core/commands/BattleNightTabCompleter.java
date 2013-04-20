package me.limebyte.battlenight.core.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class BattleNightTabCompleter implements TabCompleter {
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], CommandManager.getMainChoices(), new ArrayList<String>());
        }
        
        if (args.length == 2) {
            for (BattleNightCommand command : CommandManager.getCommands()) {
                if (command.labelMatches(args[0]) || command.aliasMatches(args[0])) {
                    return StringUtil.copyPartialMatches(args[1], command.getPrimaryChoices(), new ArrayList<String>());
                }
            }
        }
        
        return ImmutableList.of();
    }

}
