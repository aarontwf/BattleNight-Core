package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.chat.StandardPage;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionCommand extends BattleNightCommand {

    protected VersionCommand() {
        super("Version");

        this.setLabel("version");
        this.setDescription("Gets the version of BattleNight running on the server.");
        this.setAliases(Arrays.asList("ver", "about"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        PluginDescriptionFile pdf = BattleNight.getInstance().getDescription();

        StandardPage versionPage = new StandardPage(
                "BattleNight Version Info",
                "This server is running BattleNight version " + pdf.getVersion() + ".  " +
                        "For more information about Battlenight and this version, please visit:\n" +
                        ChatColor.BLUE + ChatColor.UNDERLINE + pdf.getWebsite());
        sender.sendMessage(versionPage.getPage());
        return true;
    }

}
