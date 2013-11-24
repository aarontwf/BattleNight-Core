package me.limebyte.battlenight.core.old;

import java.util.List;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.battle.Battle;
import me.limebyte.battlenight.api.battle.Lobby;
import me.limebyte.battlenight.api.util.Messenger;
import me.limebyte.battlenight.core.old.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class CheatListener extends APIRelatedListener {

    public CheatListener(BattleNightAPI api) {
        super(api);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        BattlePlayer bPlayer = BattlePlayer.get(player.getName());
        if (!bPlayer.isAlive()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Battle battle = getAPI().getBattle();

        BattlePlayer bPlayer = BattlePlayer.get(player.getName());
        if (!bPlayer.isAlive()) {
            event.setCancelled(true);
        }

        if (battle != null && battle.containsPlayer(player) && event.getSlotType() == SlotType.OUTSIDE) {
            ItemStack cursor = event.getCursor();
            if (cursor != null && cursor.getType() != Material.AIR) {
                event.setCancelled(true);
                Messenger messenger = getAPI().getMessenger();
                messenger.tell(player, messenger.get("battle.no-cheating"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!ConfigManager.get(Config.MAIN).getBoolean("Commands.Block", true)) return;

        Player player = event.getPlayer();
        Battle battle = getAPI().getBattle();

        if (getAPI().getLobby().contains(player) || battle != null && battle.containsPlayer(player)) {
            List<String> whitelist = ConfigManager.get(Config.MAIN).getStringList("Commands.Whitelist");
            whitelist.add("bn");

            String[] cmdArg = event.getMessage().split(" ");
            String cmdString = cmdArg[0].trim().substring(1).toLowerCase();

            Command command = Bukkit.getServer().getPluginCommand(cmdString);
            if (command != null) {
                // Check if the command is listed
                if (whitelist.contains(command.getLabel().toLowerCase())) return;

                // Check if there are any aliases listed
                if (!command.getAliases().isEmpty()) {
                    for (String alias : command.getAliases()) {
                        if (whitelist.contains(alias.toLowerCase())) return;
                    }
                }
            } else {
                // Check if the command is listed
                if (whitelist.contains(cmdString.toLowerCase())) return;
            }

            // Its not listed so block it
            event.setCancelled(true);
            Messenger messenger = getAPI().getMessenger();
            messenger.tell(player, messenger.get("general.no-command"));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Battle battle = getAPI().getBattle();
        BattlePlayer bPlayer = BattlePlayer.get(player.getName());

        if (!bPlayer.isAlive() || battle != null && battle.containsPlayer(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Lobby lobby = getAPI().getLobby();
        Battle battle = getAPI().getBattle();

        if (!Teleporter.telePass.contains(player.getName())) {
            if (lobby.contains(player)) {
                event.setCancelled(true);
                return;
            }

            if (battle != null && battle.containsPlayer(player)) {
                Messenger messenger = getAPI().getMessenger();

                switch (event.getCause()) {
                    case COMMAND:
                        if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.Commands", false)) {
                            event.setCancelled(true);
                            messenger.tell(player, messenger.get("general.no-teleport"));
                        }
                        break;
                    case PLUGIN:
                        if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.Plugins", false)) {
                            event.setCancelled(true);
                            messenger.tell(player, messenger.get("general.no-teleport"));
                        }
                        break;
                    case ENDER_PEARL:
                        if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.EnderPearls", true)) {
                            event.setCancelled(true);
                            messenger.tell(player, messenger.get("general.no-teleport"));
                        }
                        break;
                    case NETHER_PORTAL:
                    case END_PORTAL:
                        if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.Portals", true)) {
                            event.setCancelled(true);
                            messenger.tell(player, messenger.get("general.no-teleport"));
                        }
                        break;
                    case UNKNOWN:
                        if (!ConfigManager.get(Config.MAIN).getBoolean("Teleportation.Unknown", true)) {
                            event.setCancelled(true);
                            messenger.tell(player, messenger.get("general.no-teleport"));
                        }
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof Player) {
            Player thrower = (Player) projectile.getShooter();

            BattlePlayer bPlayer = BattlePlayer.get(thrower.getName());
            if (!bPlayer.isAlive()) {
                event.setCancelled(true);
            }
        }
    }
}
