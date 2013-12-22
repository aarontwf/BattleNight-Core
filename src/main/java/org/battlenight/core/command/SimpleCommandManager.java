package org.battlenight.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.battlenight.api.BattleAPI;
import org.battlenight.api.command.BattleNightCommand;
import org.battlenight.api.command.CommandManager;
import org.battlenight.api.message.Messenger;
import org.battlenight.core.command.defaults.TestCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.google.common.collect.Maps;

public class SimpleCommandManager implements CommandManager {

    private BattleAPI api;
    private Map<String, BattleNightCommand> commands;

    public SimpleCommandManager(BattleAPI api) {
        this.api = api;
        this.commands = Maps.newHashMap();

        // BattleNight Defaults
        registerDefault(new TestCommand());
    }

    @Override
    public BattleNightCommand getCommand(String label) {
        for (BattleNightCommand command : commands.values()) {
            if (command.matches(label)) return command;
        }
        return null;
    }

    @Override
    public void register(BattleNightCommand command) {
        command.setApi(api);
        commands.put(command.getLabel(), command);
    }

    private void registerDefault(BattleNightCommand command) {
        command.setUsage("command." + command.getLabel() + ".usage");
        command.setDescription("command." + command.getLabel() + ".description");
        register(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Messenger messenger = api.getMessenger();

        if (args.length < 1) {
            messenger.send(sender, "command.general.incorrect-usage");
            return true;
        }

        BattleNightCommand command = getCommand(args[0]);

        if (command == null) {
            messenger.send(sender, "command.general.unknown");
            return true;
        }

        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            messenger.send(sender, "command.general.player-only");
            return true;
        }

        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            messenger.send(sender, "command.general.no-permission");
            return true;
        }

        if (!command.perform(sender, Arrays.copyOfRange(args, 1, args.length))) {
            messenger.send(sender, command.getUsage(), label, args[0]);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return StringUtil.copyPartialMatches(args[0], commands.keySet(), new ArrayList<String>());
        BattleNightCommand command = getCommand(args[0]);
        return command != null ? command.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length)) : new ArrayList<String>();
    }
}
