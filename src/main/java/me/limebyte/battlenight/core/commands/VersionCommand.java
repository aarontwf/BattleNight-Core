package me.limebyte.battlenight.core.commands;

import java.util.Arrays;

import me.limebyte.battlenight.api.commands.BattleNightCommand;
import me.limebyte.battlenight.api.tosort.StandardPage;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.tosort.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class VersionCommand extends BattleNightCommand {

    protected VersionCommand() {
        super("Version");

        setLabel("version");
        setDescription("Gets the version of BattleNight running on the server.");
        setAliases(Arrays.asList("ver", "about"));
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        PluginDescriptionFile pdf = BattleNight.instance.getDescription();

        StandardPage page = new StandardPage("BattleNight Version Info", "This server is running BattleNight version " + pdf.getVersion() + ".  "
                + "For more information about Battlenight and this version, please visit:\n" + ChatColor.BLUE + ChatColor.UNDERLINE + pdf.getWebsite());
        Messenger.tell(sender, page);
        return true;
    }

}
