package me.limebyte.battlenight.core.listeners;

import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.chat.Messaging.Message;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChanger implements Listener {

    // Get Main Class
    public static BattleNight plugin;

    public SignChanger(BattleNight instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (BattleNight.ClassList != null) {
            if (BattleNight.ClassList.contains(e.getLine(0))) {
                if (!e.getLine(1).isEmpty() || !e.getLine(2).isEmpty() || !e.getLine(3).isEmpty()) {
                    Messaging.tell(player, Message.UNSUCCESSFUL_SIGN, e.getLine(0));
                    return;
                }

                e.setLine(1, "----------");
                Messaging.tell(player, Message.SUCCESSFUL_SIGN, e.getLine(0));
                return;
            }
        }
    }
}