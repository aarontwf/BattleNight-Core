package me.limebyte.battlenight.core.Listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.ParticleEffect;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public SignListener(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();
            if ((block.getState() instanceof Sign)) {
                Sign sign = (Sign) block.getState();
                if ((BattleNight.BattleClasses.containsKey(sign.getLine(0))) && (BattleNight.getBattle().usersTeam.containsKey(player.getName()))) {
                    plugin.BattleSigns.put(player.getName(), sign);
                    if (BattleNight.getBattle().usersClass.containsKey(player.getName())) {
                        if (BattleNight.getBattle().usersClass.get(player.getName()) == sign.getLine(0)) {
                            BattleNight.getBattle().usersClass.remove(player.getName());
                            if (sign.getLine(2) == player.getName()) {
                                sign.setLine(2, "");
                                sign.update();
                                plugin.reset(player, true);
                                ParticleEffect.spiral(player);
                            } else if (sign.getLine(3) == player.getName()) {
                                sign.setLine(3, "");
                                sign.update();
                                plugin.reset(player, true);
                                ParticleEffect.spiral(player);
                            } else {
                                BattleNight.tellPlayer(player, "Please tell developer about this bug (SignListener).");
                            }
                        } else {
                            BattleNight.tellPlayer(player, "You must first remove yourself from the other class!");
                        }
                    } else if (sign.getLine(2).trim().equals("")) {
                        BattleNight.getBattle().usersClass.put(player.getName(), sign.getLine(0));
                        sign.setLine(2, player.getName());
                        sign.update();
                        plugin.giveItems(player);
                        ParticleEffect.spiral(player);
                    } else if (sign.getLine(3).trim().equals("")) {
                        BattleNight.getBattle().usersClass.put(player.getName(), sign.getLine(0));
                        sign.setLine(3, player.getName());
                        sign.update();
                        plugin.giveItems(player);
                        ParticleEffect.spiral(player);
                    } else {
                        BattleNight.tellPlayer(player, "There are too many of this class, pick another class.");
                    }
                }
            }
        }
    }
}