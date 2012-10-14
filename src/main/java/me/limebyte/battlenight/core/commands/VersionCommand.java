package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.chat.StandardPage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCommand extends BattleNightCommand {

    public VersionCommand(CommandSender sender, String[] args) {
        super(sender, args);
    }

    @Override
    public boolean onPerformed() {
        StandardPage versionPage = new StandardPage(
                "BattleNight Version Info",
                "This server is running BattleNight version " + BattleNight.getVersion() + ".  " +
                        "For more information about Battlenight and this version, please visit:\n" +
                        ChatColor.BLUE + ChatColor.UNDERLINE + BattleNight.getWebsite());

        getSender().sendMessage(versionPage.getPage());
        return true;
    }

    @Override
    public CommandPermission getPermission() {
        return null;
    }

    @Override
    public String getUsage() {
        return "/bn version";
    }

    @Override
    public String getConsoleUsage() {
        return "/bn version";
    }

}
