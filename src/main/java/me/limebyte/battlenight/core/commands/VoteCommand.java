package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.managers.ScoreManager.ScoreboardState;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.core.tosort.Metadata;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoteCommand extends BattleNightCommand {

    public VoteCommand() {
        super("Vote");

        setLabel("vote");
        setDescription("Vote for an arena.");
        setUsage("/bn vote [arena]");
        setPermission(CommandPermission.USER);
    }

    @Override
    protected boolean onPerformed(CommandSender sender, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!api.getLobby().contains(player)) {
                api.getMessenger().tell(sender, Message.NOT_IN_LOBBY);
                return false;
            }

            if (api.getScoreManager().getState() != ScoreboardState.VOTING) {
                api.getMessenger().tell(sender, ChatColor.RED + "Voting has ended.");
                return false;
            }

            if (Metadata.getBoolean(player, "voted")) {
                api.getMessenger().tell(sender, ChatColor.RED + "You have already voted!");
                return false;
            }

            try {
                int id = Integer.parseInt(args[0]) - 1;
                Arena arena = api.getScoreManager().getVotableArenas().get(id);
                arena.addVote();
                Metadata.set(player, "voted", true);
                Metadata.set(player, "vote", id);
                return true;
            } catch (Exception e) {
                api.getMessenger().tell(sender, Message.INVALID_ARENA);
            }
        } else {
            api.getMessenger().tell(sender, Message.PLAYER_ONLY);
        }

        return false;
    }
}
