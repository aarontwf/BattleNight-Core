package me.limebyte.battlenight.core.util;

import java.util.List;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.util.Message;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.api.util.Page;
import me.limebyte.battlenight.api.util.Song;
import me.limebyte.battlenight.core.BattleNight;
import me.limebyte.battlenight.core.battle.SimpleArena;
import me.limebyte.battlenight.core.battle.SimpleTeam;
import me.limebyte.battlenight.core.battle.SimpleTeamedBattle;
import me.limebyte.battlenight.core.tosort.ConfigManager;
import me.limebyte.battlenight.core.tosort.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SimpleMessenger implements Messenger {

    public final String PREFIX = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;

    private BattleNightAPI api;

    public SimpleMessenger(BattleNightAPI api) {
        this.api = api;
    }

    @Override
    public void debug(Level level, String message) {
        if (ConfigManager.get(Config.MAIN).getBoolean("Debug", false)) {
            log(level, message);
        }
    }

    @Override
    public String format(Message message, Object... args) {
        return format(message.getMessage(), args);
    }

    @Override
    public String format(String message, Object... args) {
        for (int i = 0; i < args.length; i++) {
            message = message.replace("$" + (i + 1), describeObject(args[i]));
        }

        return message;
    }

    @Override
    public String getColouredName(Player player) {
        String name = player.getName();
        Battle battle = api.getBattleManager().getActiveBattle();

        if (battle.containsPlayer(player)) {
            ChatColor teamColour = ChatColor.WHITE;
            if (battle instanceof SimpleTeamedBattle) {
                teamColour = ((SimpleTeamedBattle) battle).getTeam(player).getColour();
            }
            return teamColour + name;
        } else {
            return ChatColor.DARK_GRAY + name;
        }
    }

    @Override
    public void log(Level level, String message) {
        BattleNight.instance.getLogger().log(level, message);
    }

    @Override
    public void playSong(Song battleEnd) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                battleEnd.play(p);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                battleEnd.play(p);
            }
        }
    }

    /** Sounds **/

    @Override
    public void playSound(Sound sound, float pitch) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                p.playSound(p.getLocation(), sound, 20f, pitch);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                p.playSound(p.getLocation(), sound, 20f, pitch);
            }
        }
    }

    /** Messages **/

    @Override
    public void tell(CommandSender sender, Message message) {
        tell(sender, message.getMessage());
    }

    @Override
    public void tell(CommandSender sender, Message message, Object... args) {
        tell(sender, message.getMessage(), args);
    }

    @Override
    public void tell(CommandSender sender, Page page) {
        sender.sendMessage(page.getPage());
    }

    @Override
    public void tell(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + message);
    }

    @Override
    public void tell(CommandSender sender, String message, Object... args) {
        tell(sender, format(message, args));

    }

    @Override
    public void tellEveryone(Message message) {
        tellEveryone(message.getMessage());
    }

    @Override
    public void tellEveryone(Message message, Object... args) {
        tellEveryone(format(message, args));
    }

    @Override
    public void tellEveryone(Page page) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                tell(p, page);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                tell(p, page);
            }
        }
    }

    @Override
    public void tellEveryone(String message) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                tell(p, message);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null) {
                tell(p, message);
            }
        }
    }

    @Override
    public void tellEveryone(String message, Object... args) {
        tellEveryone(format(message, args));
    }

    @Override
    public void tellEveryoneExcept(Player player, Message message) {
        tellEveryoneExcept(player, message.getMessage());
    }

    @Override
    public void tellEveryoneExcept(Player player, Message message, Object... args) {
        tellEveryoneExcept(player, message.getMessage(), args);
    }

    @Override
    public void tellEveryoneExcept(Player player, Page page) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && player != p) {
                tell(p, page);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && player != p) {
                tell(p, page);
            }
        }
    }

    @Override
    public void tellEveryoneExcept(Player player, String message) {
        for (String name : api.getBattleManager().getActiveBattle().getPlayers()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && player != p) {
                tell(p, message);
            }
        }

        for (String name : api.getSpectatorManager().getSpectators()) {
            Player p = Bukkit.getPlayerExact(name);
            if (p != null && player != p) {
                tell(p, message);
            }
        }
    }

    @Override
    public void tellEveryoneExcept(Player player, String message, Object... args) {
        tellEveryoneExcept(player, format(message, args));

    }

    private String describeEntity(Entity entity) {
        if (entity instanceof Player) return ((Player) entity).getName();

        return entity.getType().toString().toLowerCase().replace("_", " ");
    }

    private String describeLocation(Location loc) {
        return loc.getX() + ", " + loc.getY() + ", " + loc.getZ();
    }

    private String describeMaterial(Material material) {
        if (material == Material.INK_SACK) return "dye";

        return material.toString().toLowerCase().replace("_", " ");
    }

    private String describeObject(Object obj) {
        if (obj instanceof ComplexEntityPart)
            return describeObject(((ComplexEntityPart) obj).getParent());
        else if (obj instanceof Item)
            return describeMaterial(((Item) obj).getItemStack().getType());
        else if (obj instanceof ItemStack)
            return describeMaterial(((ItemStack) obj).getType());
        else if (obj instanceof Player)
            return getColouredName((Player) obj);
        else if (obj instanceof Entity)
            return describeEntity((Entity) obj);
        else if (obj instanceof Block)
            return describeMaterial(((Block) obj).getType());
        else if (obj instanceof Material)
            return describeMaterial((Material) obj);
        else if (obj instanceof Location)
            return describeLocation((Location) obj);
        else if (obj instanceof World)
            return ((World) obj).getName();
        else if (obj instanceof SimpleTeam)
            return ((SimpleTeam) obj).getColour() + ((SimpleTeam) obj).getDisplayName();
        else if (obj instanceof List<?>)
            return ((List<?>) obj).toString().replaceAll("\\[|\\]", "").replaceAll("[,]([^,]*)$", " and$1");
        else if (obj instanceof SimpleArena) return ((SimpleArena) obj).getDisplayName();
        return obj.toString();
    }

    @Override
    public void debug(Level level, String message, Object... args) {
        debug(level, format(message, args));
    }

    @Override
    public void log(Level level, String message, Object... args) {
        log(level, format(message, args));
    }

}
