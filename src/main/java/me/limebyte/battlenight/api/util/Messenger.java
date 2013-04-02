package me.limebyte.battlenight.api.util;

import java.util.logging.Level;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface Messenger {

    public void debug(Level level, String message);

    public void debug(Level level, String message, Object... args);

    public String format(Message message, Object... args);

    public String format(String message, Object... args);

    public String getColouredName(Player player);

    public void log(Level level, String message);

    public void log(Level level, String message, Object... args);

    public void playSong(Song song);

    public void playSound(Sound sound, float pitch);

    public void tell(CommandSender sender, Message message);

    public void tell(CommandSender sender, Message message, Object... args);

    public void tell(CommandSender sender, Page page);

    public void tell(CommandSender sender, String message);

    public void tell(CommandSender sender, String message, Object... args);

    public void tellEveryone(Message message);

    public void tellEveryone(Message message, Object... args);

    public void tellEveryone(Page page);

    public void tellEveryone(String message);

    public void tellEveryone(String message, Object... args);

    public void tellEveryoneExcept(Player player, Message message);

    public void tellEveryoneExcept(Player player, Message message, Object... args);

    public void tellEveryoneExcept(Player player, Page page);

    public void tellEveryoneExcept(Player player, String message);

    public void tellEveryoneExcept(Player player, String message, Object... args);

}
