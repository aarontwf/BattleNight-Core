package me.limebyte.battlenight.core.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import me.limebyte.battlenight.api.battle.PlayerClass;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.managers.ClassManager;
import me.limebyte.battlenight.core.util.ClassSign;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;
import me.limebyte.battlenight.core.util.Metadata;
import me.limebyte.battlenight.core.util.ParticleEffect;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SignListener implements Listener {

    public static Set<ClassSign> classSigns = new HashSet<ClassSign>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            if (block.getState() instanceof Sign) {
                Sign sign = (Sign) block.getState();
                String name = player.getName();
                String title = sign.getLine(0);
                HashMap<String, PlayerClass> classes = ClassManager.getClassNames();

                if (classes.containsKey(title) && BattleNight.getBattle().usersTeam.containsKey(name)) {
                    PlayerClass playerClass = classes.get(title);

                    if (player.hasPermission(playerClass.getPermission())) {
                        addName(player, sign);

                        if (Metadata.getBattleClass(player, "class") != playerClass) {
                            Messenger.debug(Level.INFO, "Making particles...");
                            ParticleEffect.classSelect(player, ConfigManager.get(Config.MAIN).getString("Particles.ClassSelection", "smoke"));
                        }

                        Metadata.set(player, "class", playerClass.getName());
                        reset(player);
                        classes.get(title).equip(player);
                    } else {
                        Messenger.tell(player, Message.NO_PERMISSION_CLASS);
                    }
                }
            }
        }
    }

    private static void addName(Player player, Sign sign) {
        getClassSign(sign).add(player);
    }

    public static void cleanSigns() {
        for (ClassSign s : classSigns) {
            s.clear();
        }
    }

    public static void cleanSigns(Player player) {
        for (ClassSign s : classSigns) {
            s.remove(player);
        }
    }

    private static ClassSign getClassSign(Sign sign) {
        for (ClassSign s : classSigns) {
            if (s.getSign().equals(sign)) return s;
        }

        ClassSign s = new ClassSign(sign);
        classSigns.add(s);
        return s;
    }

    private static void reset(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.addPotionEffect(new PotionEffect(effect.getType(), 0, 0), true);
        }
    }
}