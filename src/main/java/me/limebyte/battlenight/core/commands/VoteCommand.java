package me.limebyte.battlenight.core.commands;

import me.limebyte.battlenight.api.battle.Arena;
import me.limebyte.battlenight.api.managers.ScoreManager.ScoreboardState;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.util.player.Metadata;

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
        if (args.length < 1) return false;
        Messenger messenger = api.getMessenger();

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (!api.getLobby().contains(player)) {
                messenger.tell(sender, messenger.get("lobby.not-in"));
                return false;
            }

            if (api.getScoreManager().getState() != ScoreboardState.VOTING) {
                messenger.tell(sender, ChatColor.RED + "Voting has ended.");
                return false;
            }

            int prev = Metadata.getInt(player, "vote");
            if (prev >= 0) {
                Arena arena = api.getScoreManager().getVotableArenas().get(prev);
                if (arena != null) arena.removeVote();
            }

            try {
                int id = Integer.parseInt(args[0]) - 1;
                Arena arena = api.getScoreManager().getVotableArenas().get(id);
                arena.addVote();
                Metadata.set(player, "vote", id);
                return true;
            } catch (Exception e) {
                messenger.tell(sender, Message.INVALID_ARENA);
            }
        } else {
            messenger.tell(sender, messenger.get("command.player-only"));
        }

        return false;
    }
}
