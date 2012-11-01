package me.limebyte.battlenight.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

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
import me.limebyte.battlenight.core.listeners.DropListener;
import me.limebyte.battlenight.core.listeners.NameplateListener;
import me.limebyte.battlenight.core.listeners.ReadyListener;
import me.limebyte.battlenight.core.listeners.RespawnListener;
import me.limebyte.battlenight.core.listeners.SignChanger;
import me.limebyte.battlenight.core.listeners.SignListener;
import me.limebyte.battlenight.core.other.Tracks.Track;
import me.limebyte.battlenight.core.util.SafeTeleporter;
import me.limebyte.battlenight.core.util.config.ConfigManager;
import me.limebyte.battlenight.core.util.config.ConfigManager.Config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.kitteh.tag.TagAPI;

public class BattleNight extends JavaPlugin {

    // Variables
    public static BattleNight instance;
    public static Logger log;
    public static final String BNTag = ChatColor.GRAY + "[BattleNight] " + ChatColor.WHITE;
    public static final String BNKTag = ChatColor.GRAY + "[BattleNight KillFeed] " + ChatColor.WHITE;
    public static Set<String> ClassList;

    // HashMaps
    public static final Map<String, String> BattleClasses = new HashMap<String, String>();
    public static final Map<String, String> BattleArmor = new HashMap<String, String>();
    public static final Map<String, String> BattleTelePass = new HashMap<String, String>();

    // Other Classes
    public static Battle battle;

    public boolean redTeamIronClicked = false;
    public boolean blueTeamIronClicked = false;
    public static boolean playersInLounge = false;

    // ///////////////////
    // Plug-in Enable //
    // ////////////////////
    @Override
    public void onEnable() {

        // Set instances
        instance = this;
        log = getLogger();

        battle = new Battle();

        ConfigManager.initConfigurations();
        reloadClasses();

        // Metrics
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }

        // Debug
        if (ConfigManager.get(Config.MAIN).getBoolean("Debug", false)) {
            if (ConfigManager.get(Config.MAIN).getBoolean("UsePermissions", false)) {
                log.info("Permissions Enabled.");
            } else {
                log.info("Permissions Disabled, using Op.");
            }
            log.info("Classes: " + BattleClasses);
            log.info("Armor: " + BattleArmor);
        }

        PluginManager pm = getServer().getPluginManager();

        if (Nameplates.init(this)) {
            // Event Registration
            PluginDescriptionFile pdfFile = getDescription();
            pm.registerEvents(new CheatListener(this), this);
            pm.registerEvents(new CommandBlocker(this), this);
            pm.registerEvents(new DamageListener(this), this);
            pm.registerEvents(new DeathListener(this), this);
            pm.registerEvents(new DisconnectListener(this), this);
            pm.registerEvents(new DropListener(this), this);
            pm.registerEvents(new NameplateListener(this), this);
            pm.registerEvents(new ReadyListener(this), this);
            pm.registerEvents(new RespawnListener(), this);
            pm.registerEvents(new SignChanger(this), this);
            pm.registerEvents(new SignListener(), this);

            // Enable Message
            log.info("Version " + pdfFile.getVersion() + " enabled successfully.");
            log.info("Made by LimeByte.");
        } else {
            pm.disablePlugin(this);
        }
    }

    // ////////////////////
    // Plug-in Disable //
    // ////////////////////
    @Override
    public void onDisable() {
        if (getBattle().isInProgress() || playersInLounge) {
            log.info("Ending current Battle...");
            battle.stop();
        }
        SignListener.cleanSigns();
        PluginDescriptionFile pdfFile = getDescription();
        log.info("Version " + pdfFile.getVersion() + " has been disabled.");
    }

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

    // Set Coords and put in waypoints.data
    public static void setCoords(Waypoint waypoint, Location location) {
        String place = waypoint.getName();
        ConfigManager.reload(Config.WAYPOINTS);
        FileConfiguration config = ConfigManager.get(Config.WAYPOINTS);
        config.set("coords." + place + ".world", location.getWorld().getName());
        config.set("coords." + place + ".x", location.getX());
        config.set("coords." + place + ".y", location.getY());
        config.set("coords." + place + ".z", location.getZ());
        config.set("coords." + place + ".yaw", location.getYaw());
        config.set("coords." + place + ".pitch", location.getPitch());
        ConfigManager.save(Config.WAYPOINTS);
    }

    // Get Coords from waypoints.data
    public static Location getCoords(String place) {
        ConfigManager.reload(Config.WAYPOINTS);
        FileConfiguration config = ConfigManager.get(Config.WAYPOINTS);
        Double x = config.getDouble("coords." + place + ".x", 0);
        Double y = config.getDouble("coords." + place + ".y", 0);
        Double z = config.getDouble("coords." + place + ".z", 0);
        String yawToParse = config.getString("coords." + place + ".yaw");
        float yaw = 0;
        if (yawToParse != null) {
            try {
                yaw = Float.parseFloat(yawToParse);
            } catch (NumberFormatException nfe) {
                // log it, do whatever you want, it's not a float. Maybe give it
                // a default value
            }
        }
        String pitchToParse = config.getString("coords." + place + ".pitch");
        float pitch = 0;
        if (pitchToParse != null) {
            try {
                pitch = Float.parseFloat(pitchToParse);
            } catch (NumberFormatException nfe) {
                // log it, do whatever you want, it's not a float. Maybe give it
                // a default value
            }
        }
        World world = Bukkit.getServer().getWorld(config.getString("coords." + place + ".world"));
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static boolean pointSet(Waypoint waypoint) {
        ConfigManager.reload(Config.WAYPOINTS);
        FileConfiguration config = ConfigManager.get(Config.WAYPOINTS);
        try {
            Set<String> set = config.getConfigurationSection("coords").getKeys(false);
            return set.contains(waypoint.getName());
        } catch (NullPointerException e) {
            return false;
        }
    }

    // Check if all Waypoints have been set.
    public static Boolean isSetup() {
        ConfigManager.reload(Config.WAYPOINTS);
        FileConfiguration config = ConfigManager.get(Config.WAYPOINTS);
        if (!config.isSet("coords")) {
            return false;
        } else {
            Set<String> set = config.getConfigurationSection("coords").getKeys(false);
            return set.size() == Waypoint.values().length;
        }
    }

    public static int numSetupPoints() {
        ConfigManager.reload(Config.WAYPOINTS);
        FileConfiguration config = ConfigManager.get(Config.WAYPOINTS);
        if (!config.isSet("coords")) {
            return 0;
        } else {
            Set<String> set = config.getConfigurationSection("coords").getKeys(false);
            return set.size();
        }
    }

    // Give Player Class Items
    public static void giveItems(Player player) {
        String playerClass = getBattle().usersClass.get(player.getName());
        String rawItems = BattleClasses.get(playerClass);
        String ArmorList = BattleArmor.get(playerClass);
        String[] items;
        items = rawItems.split(",");
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            player.getInventory().setItem(i, parseItem(item));
            if (player.getInventory().contains(ConfigManager.get(Config.CLASSES).getInt("DummyItem", 6))) {
                player.getInventory().remove(ConfigManager.get(Config.CLASSES).getInt("DummyItem", 6));
            }
        }
        // Set Armour
        // Helmets
        if (ArmorList.contains("298")) {
            player.getInventory().setHelmet(new ItemStack(298, 1));
        } else if (ArmorList.contains("302")) {
            player.getInventory().setHelmet(new ItemStack(302, 1));
        } else if (ArmorList.contains("306")) {
            player.getInventory().setHelmet(new ItemStack(306, 1));
        } else if (ArmorList.contains("310")) {
            player.getInventory().setHelmet(new ItemStack(310, 1));
        } else if (ArmorList.contains("314")) {
            player.getInventory().setHelmet(new ItemStack(314, 1));
        }
        // Chestplates
        if (ArmorList.contains("299")) {
            player.getInventory().setChestplate(new ItemStack(299, 1));
        } else if (ArmorList.contains("303")) {
            player.getInventory().setChestplate(new ItemStack(303, 1));
        } else if (ArmorList.contains("307")) {
            player.getInventory().setChestplate(new ItemStack(307, 1));
        } else if (ArmorList.contains("311")) {
            player.getInventory().setChestplate(new ItemStack(311, 1));
        } else if (ArmorList.contains("315")) {
            player.getInventory().setChestplate(new ItemStack(315, 1));
        }
        // Leggings
        if (ArmorList.contains("300")) {
            player.getInventory().setLeggings(new ItemStack(300, 1));
        } else if (ArmorList.contains("304")) {
            player.getInventory().setLeggings(new ItemStack(304, 1));
        } else if (ArmorList.contains("308")) {
            player.getInventory().setLeggings(new ItemStack(308, 1));
        } else if (ArmorList.contains("312")) {
            player.getInventory().setLeggings(new ItemStack(312, 1));
        } else if (ArmorList.contains("316")) {
            player.getInventory().setLeggings(new ItemStack(316, 1));
        }
        // Boots
        if (ArmorList.contains("301")) {
            player.getInventory().setBoots(new ItemStack(301, 1));
        } else if (ArmorList.contains("305")) {
            player.getInventory().setBoots(new ItemStack(305, 1));
        } else if (ArmorList.contains("309")) {
            player.getInventory().setBoots(new ItemStack(309, 1));
        } else if (ArmorList.contains("313")) {
            player.getInventory().setBoots(new ItemStack(313, 1));
        } else if (ArmorList.contains("317")) {
            player.getInventory().setBoots(new ItemStack(317, 1));
        }
    }

    public boolean teamReady(Team team) {
        int members = 0;
        int membersReady = 0;

        for (Entry<String, Team> entry : getBattle().usersTeam.entrySet()) {
            if (Bukkit.getPlayer(entry.getKey()) != null) {
                if (entry.getValue().equals(team)) {
                    members++;
                    if (getBattle().usersClass.containsKey(entry.getKey())) membersReady++;
                }
            }
        }

        if (members == membersReady && members > 0) {
            if (team.equals(Team.RED) || team.equals(Team.BLUE)) { return true; }
        }

        return false;
    }

    public static void tellEveryone(String msg) {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) Bukkit.getPlayer(name).sendMessage(BNTag + msg);
        }
    }

    public void tellEveryone(Track track) {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) Bukkit.getPlayer(name).sendMessage(BNTag + track.msg);
        }
    }

    public void killFeed(String msg) {
        List<Player> told = new ArrayList<Player>();

        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                currentPlayer.sendMessage(BNTag + msg);
                told.add(currentPlayer);
            }
        }

        for (String name : getBattle().spectators) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                if (!told.contains(currentPlayer)) {
                    currentPlayer.sendMessage(BNTag + msg);
                    told.add(currentPlayer);
                }
            }
        }

        told.clear();
    }

    public static void tellEveryoneExcept(Player player, String msg) {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayerExact(name) != null) {
                Player currentPlayer = Bukkit.getPlayerExact(name);
                if (currentPlayer != player) currentPlayer.sendMessage(BNTag + msg);
            }
        }
    }

    public void tellTeam(Team team, String msg) {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                if (getBattle().usersTeam.get(name).equals(team)) currentPlayer.sendMessage(BNTag + msg);
            }
        }
    }

    public void tellTeam(Team team, Track track) {
        for (String name : getBattle().usersTeam.keySet()) {
            if (Bukkit.getPlayer(name) != null) {
                Player currentPlayer = Bukkit.getPlayer(name);
                if (getBattle().usersTeam.get(name).equals(team)) currentPlayer.sendMessage(BNTag + track.msg);
            }
        }
    }

    public static void tellPlayer(Player player, String msg) {
        player.sendMessage(BNTag + msg);
    }

    public static void tellPlayer(Player player, Track track) {
        player.sendMessage(BNTag + track.msg);
    }

    public void teleportAllToSpawn() {
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

    public boolean hasEmptyInventory(Player player) {
        ItemStack[] invContents = player.getInventory().getContents();
        ItemStack[] armContents = player.getInventory().getArmorContents();
        int invNullCounter = 0;
        int armNullCounter = 0;
        for (int i = 0; i < invContents.length; i++) {
            if (invContents[i] == null) {
                invNullCounter++;
            }
        }
        for (int i = 0; i < armContents.length; i++) {
            if (armContents[i].getType() == Material.AIR) {
                armNullCounter++;
            }
        }
        return (invNullCounter == invContents.length)
                && (armNullCounter == armContents.length);
    }

    //    public static void goToWaypoint(Player player, Waypoint waypoint) {
    //        Location destination = getCoords(waypoint.getName());
    //        Chunk chunk = destination.getChunk();
    //
    //        if (!chunk.isLoaded()) {
    //            chunk.load();
    //            while (!chunk.isLoaded()) {
    //                // Wait until loaded
    //            }
    //        }
    //
    //        BattleTelePass.put(player.getName(), "yes");
    //        player.teleport(destination);
    //        BattleTelePass.remove(player.getName());
    //        TagAPI.refreshPlayer(player);
    //    }

    public static ItemStack parseItem(String rawItem) {
        if (rawItem == null || rawItem.equals(""))
            return null;

        String[] part1 = rawItem.split("x");
        String[] part2 = part1[0].split(":");
        String item = part2[0];
        if (part1.length == 1) {
            if (part2.length == 1) {
                return parseItemWithoutData(item, "1");
            } else if (part2.length == 2) {
                String data = part2[1];
                return parseItemWithData(item, data);
            }
        } else if (part1.length == 2) {
            String amount = part1[1];
            if (part2.length == 1) {
                return parseItemWithoutData(item, amount);
            } else if (part2.length == 2) {
                String data = part2[1];
                return parseItemWithData(item, data, amount);
            }
        }
        return null;
    }

    private static ItemStack parseItemWithoutData(String item, String amount) {
        Material m = Material.getMaterial(Integer.parseInt(item));
        int a = Integer.parseInt(amount);
        if (a > m.getMaxStackSize()) {
            log.warning("You attempted to set the item:" + m + " to have a greater stack size than possible.");
            a = m.getMaxStackSize();
        }
        return new ItemStack(m, a);
    }

    private static ItemStack parseItemWithData(String item, String data) {
        int i = Integer.parseInt(item);
        short d = Short.parseShort(data);

        return new ItemStack(i, 1, d);
    }

    private static ItemStack parseItemWithData(String item, String data,
            String amount) {
        Material m = Material.getMaterial(Integer.parseInt(item));
        byte d = Byte.parseByte(data);
        int a = Integer.parseInt(amount);
        if (a > m.getMaxStackSize()) {
            log.warning("You attempted to set the item:" + m + " to have a greater stack size than possible.");
            a = m.getMaxStackSize();
        }
        return new ItemStack(m, a, d);
    }

    public static void addSpectator(Player player, String type) {
        if (!type.equals("death")) {
            SafeTeleporter.tp(player, Waypoint.SPECTATOR);
        }
        getBattle().spectators.add(player.getName());
        tellPlayer(player, Track.WELCOME_SPECTATOR);
    }

    public static void removeSpectator(Player player) {
        SafeTeleporter.tp(player, Waypoint.EXIT);
        getBattle().spectators.remove(player.getName());
        tellPlayer(player, Track.GOODBYE_SPECTATOR);
    }

    public void removeAllSpectators() {
        for (String pName : getBattle().spectators) {
            if (Bukkit.getPlayer(pName) != null) {
                Player currentPlayer = Bukkit.getPlayer(pName);
                SafeTeleporter.queue(currentPlayer, Waypoint.EXIT);
            }
        }
        SafeTeleporter.startTeleporting();

        getBattle().spectators.clear();
    }

    public boolean preparePlayer(Player p) {
        String inventoryType = ConfigManager.get(Config.MAIN).getString("InventoryType", "save");
        FileConfiguration storage = ConfigManager.get(Config.PLAYERS);

        if (inventoryType.equalsIgnoreCase("prompt") && !hasEmptyInventory(p)) return false;

        String name = p.getName();

        // Inventory
        if (inventoryType.equalsIgnoreCase("save")) {
            storage.set(name + ".data.inv.main", Arrays.asList(p.getInventory().getContents()));
            storage.set(name + ".data.inv.armor", Arrays.asList(p.getInventory().getArmorContents()));
        }

        // Health
        storage.set(name + ".data.health", p.getHealth());

        // Hunger
        storage.set(name + ".data.hunger.foodlevel", p.getFoodLevel());
        storage.set(name + ".data.hunger.saturation", Float.toString(p.getSaturation()));
        storage.set(name + ".data.hunger.exhaustion", Float.toString(p.getExhaustion()));

        // Experience
        storage.set(name + ".data.exp.level", p.getLevel());
        storage.set(name + ".data.exp.ammount", Float.toString(p.getExp()));

        // GameMode
        storage.set(name + ".data.gamemode", p.getGameMode().getValue());

        // Flying
        storage.set(name + ".data.flight.allowed", p.getAllowFlight());
        storage.set(name + ".data.flight.flying", p.isFlying());

        // Sleep
        storage.set(name + ".data.sleepignored", p.isSleepingIgnored());

        // Information
        storage.set(name + ".data.info.displayname", p.getDisplayName());
        storage.set(name + ".data.info.listname", p.getPlayerListName());

        // Statistics
        storage.set(name + ".data.stats.tickslived", p.getTicksLived());
        storage.set(name + ".data.stats.nodamageticks", p.getNoDamageTicks());

        // State
        storage.set(name + ".data.state.remainingair", p.getRemainingAir());
        storage.set(name + ".data.state.falldistance", Float.toString(p.getFallDistance()));
        storage.set(name + ".data.state.fireticks", p.getFireTicks());

        ConfigManager.save(Config.PLAYERS);

        // Reset Player
        reset(p, false);
        return true;
    }

    public void restorePlayer(Player p) {
        String name = p.getName();
        reset(p, true);

        String inventoryType = ConfigManager.get(Config.MAIN).getString("InventoryType", "save");
        ConfigManager.reload(Config.PLAYERS);
        FileConfiguration storage = ConfigManager.get(Config.PLAYERS);

        try {
            // Inventory
            if (inventoryType.equalsIgnoreCase("save")) {
                p.getInventory().setContents(storage.getList(name + ".data.inv.main").toArray(new ItemStack[0]));
                p.getInventory().setArmorContents(storage.getList(name + ".data.inv.armor").toArray(new ItemStack[0]));
            }

            // Health
            p.setHealth(storage.getInt(name + ".data.health"));

            // Hunger
            p.setFoodLevel(storage.getInt(name + ".data.hunger.foodlevel"));
            p.setSaturation(Float.parseFloat(storage.getString(name + ".data.hunger.saturation")));
            p.setExhaustion(Float.parseFloat(storage.getString(name + ".data.hunger.exhaustion")));

            // Experience
            p.setLevel(storage.getInt(name + ".data.exp.level"));
            p.setExp(Float.parseFloat(storage.getString(name + ".data.exp.ammount")));

            // GameMode
            p.setGameMode(GameMode.getByValue(storage.getInt(name + ".data.gamemode")));

            // Flying
            p.setAllowFlight(storage.getBoolean(name + ".data.flight.allowed"));
            p.setFlying(storage.getBoolean(name + ".data.flight.flying"));

            // Sleep
            p.setSleepingIgnored(storage.getBoolean(name + ".data.sleepignored"));

            // Information
            p.setDisplayName(storage.getString(name + ".data.info.displayname"));
            p.setPlayerListName(storage.getString(name + ".data.info.listname"));

            // Statistics
            p.setTicksLived(storage.getInt(name + ".data.stats.tickslived"));
            p.setNoDamageTicks(storage.getInt(name + ".data.stats.nodamageticks"));

        } catch (NullPointerException e) {
            log.warning("Failed to restore data for player: '" + name + "'.");
        }
    }

    public static void reset(Player p, boolean light) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[inv.getArmorContents().length]);

        removePotionEffects(p);

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
        ChatColor teamColour = ChatColor.WHITE;
        if (getBattle().usersTeam.containsKey(name)) {
            teamColour = getBattle().usersTeam.get(name).equals(Team.RED) ? ChatColor.RED : ChatColor.BLUE;
        }

        player.setPlayerListName(pListName.length() < 16 ? pListName : pListName.substring(0, 16));
        player.setDisplayName(ChatColor.GRAY + "[BN] " + teamColour + name + ChatColor.RESET);
        try {
            TagAPI.refreshPlayer(player);
        } catch (Exception e) {
        }
    }

    private static void removePotionEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.addPotionEffect(new PotionEffect(effect.getType(), 0, 0), true);
        }
    }

    public static void reloadClasses() {
        ClassList = ConfigManager.get(Config.CLASSES).getConfigurationSection("Classes").getKeys(false);
        for (String className : ClassList) {
            BattleClasses.put(className, ConfigManager.get(Config.CLASSES).getString("Classes." + className + ".Items", null));
            BattleArmor.put(className, ConfigManager.get(Config.CLASSES).getString("Classes." + className + ".Armor", null));
        }
    }

    public static Battle getBattle() {
        return battle;
    }

    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    public static String getWebsite() {
        return instance.getDescription().getWebsite();
    }
}
