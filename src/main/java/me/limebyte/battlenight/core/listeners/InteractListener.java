package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.old.Team;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();

            if (block.getTypeId() == ConfigManager.get(Config.MAIN).getInt("ReadyBlock", 42)) {
                if (BattleNight.getBattle().usersTeam.containsKey(player.getName()) && BattleNight.getBattle().isInLounge()) {
                    String name = player.getName();
                    Team team = BattleNight.getBattle().usersTeam.get(name);

                    if (team.isReady()) {
                        Messenger.tellEveryone(false, Message.TEAM_IS_READY, team.getColour() + team.getName());

                        if (team.equals(Team.RED)) {
                            BattleNight.getBattle().redTeamIronClicked = true;

                            if (Team.BLUE.isReady() && BattleNight.getBattle().blueTeamIronClicked) {
                                BattleNight.getBattle().start();
                            }
                        } else if (team.equals(Team.BLUE)) {
                            BattleNight.getBattle().blueTeamIronClicked = true;

                            if (Team.RED.isReady() && BattleNight.getBattle().redTeamIronClicked) {
                                BattleNight.getBattle().start();
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE + "Your team have not all picked a class!");
                    }
                }
            }
        }

        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (BattleNight.getBattle().spectators.contains(player.getName())) {
                ItemStack stack = player.getItemInHand();
                if (stack != null && stack.getItemMeta() != null) {
                    String itemName = stack.getItemMeta().getDisplayName();
                    if (itemName.contains("Previous Player")) {
                        Messenger.tell(player, "Teleporting to previous player.");
                    } else if (itemName.contains("Next Player")) {
                        Messenger.tell(player, "Teleporting to next player.");
                    }
                }
            }
        }
    }
}