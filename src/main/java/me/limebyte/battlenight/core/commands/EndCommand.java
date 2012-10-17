package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.Other.Tracks.Track;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EndCommand extends BattleNightCommand {

    protected EndCommand() {
        super("End");

        this.setLabel("end");
        this.setDescription("Used to end a Battle.");
        this.setUsage("/bn end");
        this.setPermission(CommandPermission.MODERATOR);
        this.setAliases(Arrays.asList("stop", "kickall"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        BattleNight.getBattle().end();
        sender.sendMessage(BattleNight.BNTag + ChatColor.RED + Track.BATTLE_ENDED.msg);
        return true;
    }

}
