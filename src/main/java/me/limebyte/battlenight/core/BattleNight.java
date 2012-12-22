package me.limebyte.battlenight.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.limebyte.battlenight.api.BattleNightAPI;
import me.limebyte.battlenight.api.Util;
import me.limebyte.battlenight.core.battle.Battle;
import me.limebyte.battlenight.core.battle.Team;
import me.limebyte.battlenight.core.battle.Waypoint;
import me.limebyte.battlenight.core.commands.CommandMap;
import me.limebyte.battlenight.core.hooks.Metrics;
import me.limebyte.battlenight.core.hooks.Nameplates;
import me.limebyte.battlenight.core.listeners.CheatListener;
import me.limebyte.battlenight.core.listeners.CommandBlocker;
import me.limebyte.battlenight.core.listeners.DamageListener;
import me.limebyte.battlenight.core.listeners.DeathListener;
import me.limebyte.battlenight.core.listeners.DisconnectListener;
import me.limebyte.battlenight.core.listeners.NameplateListener;
import me.limebyte.battlenight.core.listeners.ReadyListener;
import me.limebyte.battlenight.core.listeners.RespawnListener;
import me.limebyte.battlenight.core.listeners.SignChanger;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.util.ClassManager;
import me.limebyte.battlenight.core.util.OldUtil;
import me.limebyte.battlenight.core.util.SafeTeleporter;
import me.limebyte.battlenight.core.util.chat.Messaging;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.kitteh.tag.TagAPI;

public class BattleNight extends JavaPlugin {

    /** Variables **/

    // Instance Variables
    private static BattleNight instance;
    private BattleNightAPI api;
    protected static final Util util = new SimpleUtil();
    protected static me.limebyte.battlenight.api.Battle battle;

    private static Battle oldBattle;

    public static final String BNTag = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;

    // HashMaps
    public static final Map<String, String> BattleTelePass = new HashMap<String, String>();

    // Other Variables
    public static boolean redTeamIronClicked = false;
    public static boolean blueTeamIronClicked = false;

    /** Events **/

    @Override
    public void onEnable() {
        // Set instances
        instance = this;
        api = new SimpleAPI(this);
        battle = new ClassicBattle(this);

        oldBattle = new Battle();

        ConfigManager.initConfigurations();
        ClassManager.reloadClasses();

        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }

        // Debugging
        if (ConfigManager.get(Config.MAIN).getBoolean("UsePermissions", false)) {
            Messaging.debug(Level.INFO, "Permissions Enabled.");
        } else {
            Messaging.debug(Level.INFO, "Permissions Disabled, using Op.");
        }
        String loadedClasses = ClassManager.getClassNames().keySet().toString();
        Messaging.debug(Level.INFO, "Loaded Classes: " + loadedClasses.replaceAll("\\[|\\]", "") + ".");

        PluginManager pm = getServer().getPluginManager();

        if (!Nameplates.init(this)) {
            pm.disablePlugin(this);
            return;
        }

        // Event Registration
        PluginDescriptionFile pdfFile = getDescription();
        pm.registerEvents(new CheatListener(), this);
        pm.registerEvents(new CommandBlocker(), this);
        pm.registerEvents(new DamageListener(), this);
        pm.registerEvents(new DeathListener(), this);
        pm.registerEvents(new DisconnectListener(), this);
        pm.registerEvents(new NameplateListener(), this);
        pm.registerEvents(new ReadyListener(), this);
        pm.registerEvents(new RespawnListener(), this);
        pm.registerEvents(new SafeTeleporter(), this);
        pm.registerEvents(new SignChanger(), this);
        pm.registerEvents(new SignListener(), this);

        // Enable Message
        Messaging.log(Level.INFO, "Version " + pdfFile.getVersion() + " enabled successfully.");
        Messaging.log(Level.INFO, "Made by LimeByte.");
    }

    // ////////////////////
    // Plug-in Disable //
    // ////////////////////
    @Override
    public void onDisable() {
        if (getBattle().isInProgress() || getBattle().isInLounge()) {
            Messaging.log(Level.INFO, "Ending current Battle...");
            oldBattle.stop();
        }
        SignListener.cleanSigns();

        PluginDescriptionFile pdfFile = getDescription();
        Messaging.log(Level.INFO, "Version " + pdfFile.getVersion() + " has been disabled.");
    }

    /** Commands **/

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("bn")) {
            if (args.length < 1) {
                sender.sendMessage(BNTag + ChatColor.RED + "Incorrect usage.  Type \"" + CommandMap.getCommand("Help").getUsage() + "\" to show the help menu.");
                return true;
            } else {
                return CommandMap.dispatch(sender, args);
            }
        }
        return false;
    }

    /** Methods **/

    public static boolean isSetup() {
        for (Waypoint wp : Waypoint.values()) {
            if (!wp.isSet()) return false;
        }
        return true;
    }

    public static int numSetupPoints() {
        int set = 0;
        for (Waypoint wp : Waypoint.values()) {
            if (wp.isSet()) set++;
        }
        return set;
    }

    public static void teleportAllToSpawn() {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                if (getBattle().usersTeam.get(name).equals(Team.RED)) {
                    SafeTeleporter.tp(currentPlayer, Waypoint.RED_SPAWN);
                }
                if (getBattle().usersTeam.get(name).equals(Team.BLUE)) {
                    SafeTeleporter.tp(currentPlayer, Waypoint.BLUE_SPAWN);
                }
            }
        }

        SafeTeleporter.startTeleporting();
    }

    public static boolean preparePlayer(Player player) {
        String invType = ConfigManager.get(Config.MAIN).getString("InventoryType", "save");

        if (invType.equalsIgnoreCase("prompt")) {
            if (!util.inventoryEmpty(player.getInventory())) return false;
        } else if (!invType.equalsIgnoreCase("clear")) {
            PlayerData.store(player);
        }

        SimpleUtil.reset(player);
        return true;
    }

    public static void restorePlayer(Player player) {
        SimpleUtil.reset(player);
        util.restorePlayer(player);
    }

    public static void reset(Player p, boolean light) {
        OldUtil.clearInventory(p);
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.addPotionEffect(new PotionEffect(effect.getType(), 0, 0), true);
        }

        if (!light) {
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(16);
            p.setSaturation(1000);
            p.setExhaustion(0);
            p.setLevel(0);
            p.setExp(0);
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(false);
            p.setFlying(false);
            p.setSleepingIgnored(true);

            setNames(p);

            p.setTicksLived(1);
            p.setNoDamageTicks(0);
            p.setRemainingAir(300);
            p.setFallDistance(0.0f);
            p.setFireTicks(-20);
        }
    }

    public static void setNames(Player player) {
        String name = player.getName();
        String pListName = ChatColor.GRAY + "[BN] " + name;
        player.setPlayerListName(pListName.length() < 16 ? pListName : pListName.substring(0, 16));

        try {
            TagAPI.refreshPlayer(player);
        } catch (Exception e) {
        }
    }

    public static BattleNight getInstance() {
        return instance;
    }

    public static Battle getBattle() {
        return oldBattle;
    }

    public BattleNightAPI getAPI() {
        return api;
    }
}
