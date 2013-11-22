package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.managers.ClassManager;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.PlayerClass;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;
import me.limebyte.battlenight.core.util.ParticleEffect;
import me.limebyte.battlenight.core.util.player.Metadata;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand extends BattleNightCommand {

    protected ClassCommand() {
        super("Class");

        setLabel("class");
        setDescription("Selects a class.");
        setUsage("/bn class <class>");
        setPermission(CommandPermission.USER);
        setAliases(Arrays.asList("equip"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        Messenger messenger = api.getMessenger();
        Lobby lobby = api.getLobby();

        if (!(sender instanceof Player)) {
            messenger.tell(sender, messenger.get("command.player-only"));
            return false;
        }

        Player player = (Player) sender;

        if (!lobby.contains(player)) {
            messenger.tell(sender, messenger.get("lobby.not-in"));
            return false;
        }

        if (args.length < 1) {
            messenger.tell(sender, messenger.get("class.specify"));
            messenger.tell(sender, messenger.get("command.usage"), getUsage());
            return false;
        }

        ClassManager manager = api.getClassManager();
        boolean random = args[0].equalsIgnoreCase("random");
        PlayerClass playerClass = random ? manager.getRandomClass() : manager.getPlayerClass(args[0]);

        if (playerClass == null) {
            messenger.tell(sender, messenger.get("class.invalid"), args[0]);
            return false;
        }

        if (player.hasPermission(playerClass.getPermission())) {
            if (Metadata.getPlayerClass(player) != playerClass) {
                ParticleEffect.classSelect(player, ConfigManager.get(Config.MAIN).getString("Particles.ClassSelection", "smoke"));
            }

            api.setPlayerClass(player, playerClass);
            return true;
        } else {
            messenger.tell(player, messenger.get("class.no-permission"));
            return false;
        }

    }

}
