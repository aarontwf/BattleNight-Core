package me.limebyte.battlenight.core.listeners;

import java.util.HashMap;

import me.limebyte.battlenight.api.battle.PlayerClass;
import me.limebyte.battlenight.core.util.ClassManager;
import me.limebyte.battlenight.core.util.Messenger;
import me.limebyte.battlenight.core.util.Messenger.Message;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChanger implements Listener {

    private static final String LINE = "----------";

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        String title = e.getLine(0);
        HashMap<String, PlayerClass> classes = ClassManager.getClassNames();

        if (classes != null) {
            if (classes.containsKey(title)) {
                if (!e.getLine(1).isEmpty() || !e.getLine(2).isEmpty() || !e.getLine(3).isEmpty()) {
                    Messenger.tell(player, Message.UNSUCCESSFUL_SIGN, title);
                    return;
                }

                e.setLine(1, LINE);
                Messenger.tell(player, Message.SUCCESSFUL_SIGN, title);
                return;
            }
        }
    }
}