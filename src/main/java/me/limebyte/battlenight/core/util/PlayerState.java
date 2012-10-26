//package me.limebyte.battlenight.core.util;
//
//import java.util.Arrays;
//
//import org.bukkit.GameMode;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
//import org.bukkit.inventory.ItemStack;
//
//public class PlayerState {
//    private static void reloadStats(Player p) {
//        FileConfiguration config = ConfigurationManager.get(Config.PLAYERS);
//
//        if (config.getConfigurationSection(p.getName()) == null) {
//            config.set(p.getName() + ".stats.kills", obfuscate(0));
//            config.set(p.getName() + ".stats.deaths", obfuscate(0));
//        }
//    }
//
//    public static void save(Player p) {
//        FileConfiguration config = ConfigurationManager.get(Config.PLAYERS);
//
//        String name = p.getName();
//
//        // Reload the config
//        ConfigurationManager.reload(Config.PLAYERS);
//
//        // Statistics
//        reloadStats(p);
//
//        // Inventory
//        config.set(name + ".data.inv.main", Arrays.asList(p.getInventory().getContents()));
//        config.set(name + ".data.inv.armor", Arrays.asList(p.getInventory().getArmorContents()));
//
//        // Health
//        config.set(name + ".data.health", p.getHealth());
//
//        // Hunger
//        config.set(name + ".data.hunger.foodlevel", p.getFoodLevel());
//        config.set(name + ".data.hunger.saturation", Float.toString(p.getSaturation()));
//        config.set(name + ".data.hunger.exhaustion", Float.toString(p.getExhaustion()));
//
//        // Experience
//        config.set(name + ".data.exp.level", p.getLevel());
//        config.set(name + ".data.exp.ammount", Float.toString(p.getExp()));
//
//        // GameMode
//        config.set(name + ".data.gamemode", p.getGameMode().getValue());
//
//        // Flying
//        config.set(name + ".data.flight.allowed", p.getAllowFlight());
//        config.set(name + ".data.flight.flying", p.isFlying());
//
//        // Locations
//        config.set(name + ".data.location", Util.locationToString(p.getLocation()));
//
//        // Sleep
//        config.set(name + ".data.sleepignored", p.isSleepingIgnored());
//
//        // Information
//        config.set(name + ".data.info.displayname", p.getDisplayName());
//        config.set(name + ".data.info.listname", p.getPlayerListName());
//
//        // State
//        config.set(name + ".data.stats.tickslived", p.getTicksLived());
//        config.set(name + ".data.stats.nodamageticks", p.getNoDamageTicks());
//        config.set(name + ".data.state.remainingair", p.getRemainingAir());
//        config.set(name + ".data.state.falldistance", Float.toString(p.getFallDistance()));
//        config.set(name + ".data.state.fireticks", p.getFireTicks());
//
//        // Save it all
//        ConfigurationManager.save(Config.PLAYERS);
//    }
//
//    public static void restore(Player p) {
//        FileConfiguration config = ConfigurationManager.get(Config.PLAYERS);
//
//        String name = p.getName();
//
//        // Reload the config
//        ConfigurationManager.reload(Config.PLAYERS);
//
//        // Inventory
//        p.getInventory().setContents(config.getList(name + ".data.inv.main").toArray(new ItemStack[0]));
//        p.getInventory().setArmorContents(config.getList(name + ".data.inv.armor").toArray(new ItemStack[0]));
//
//        // Health
//        p.setHealth(config.getInt(name + ".data.health"));
//
//        // Hunger
//        p.setFoodLevel(config.getInt(name + ".data.hunger.foodlevel"));
//        p.setSaturation(Float.parseFloat(config.getString(name + ".data.hunger.saturation")));
//        p.setExhaustion(Float.parseFloat(config.getString(name + ".data.hunger.exhaustion")));
//
//        // Experience
//        p.setLevel(config.getInt(name + ".data.exp.level"));
//        p.setExp(Float.parseFloat(config.getString(name + ".data.exp.ammount")));
//
//        // GameMode
//        p.setGameMode(GameMode.getByValue(config.getInt(name + ".data.gamemode")));
//
//        // Flying
//        p.setAllowFlight(config.getBoolean(name + ".data.flight.allowed"));
//        p.setFlying(config.getBoolean(name + ".data.flight.flying"));
//
//        // Locations
//        p.teleport(Util.locationFromString(config.getString(name + ".data.location")), TeleportCause.PLUGIN);
//
//        // Sleep
//        p.setSleepingIgnored(config.getBoolean(name + ".data.sleepignored"));
//
//        // Information
//        p.setDisplayName(config.getString(name + ".data.info.displayname"));
//        p.setPlayerListName(config.getString(name + ".data.info.listname"));
//
//        // Statistics
//        p.setTicksLived(config.getInt(name + ".data.stats.tickslived"));
//        p.setNoDamageTicks(config.getInt(name + ".data.stats.nodamageticks"));
//
//        // State
//        p.setRemainingAir(config.getInt(name + ".data.state.remainingair"));
//        p.setFallDistance(Float.parseFloat(config.getString(name + ".data.state.falldistance")));
//        p.setFireTicks(config.getInt(name + ".data.state.fireticks"));
//    }
//
//    public static void reset(Player p/** , Battle b **/
//    ) {
//        Util.clearInventory(p);
//        p.setHealth(p.getMaxHealth());
//        p.setFoodLevel(16);
//        p.setSaturation(1000);
//        p.setExhaustion(0);
//        p.setLevel(0);
//        p.setExp(0);
//        p.setGameMode(GameMode.SURVIVAL);
//        p.setAllowFlight(false);
//        p.setFlying(false);
//        p.setSleepingIgnored(true);
//        //TODO p.setDisplayName(ChatColor.GRAY + "[BN] " + team.getChatColor() + p.getName() + ChatColor.RESET);
//        //TODO Util.setPlayerListName(p, t);
//        p.setTicksLived(1);
//        p.setNoDamageTicks(0);
//        p.setRemainingAir(300);
//        p.setFallDistance(0.0f);
//        p.setFireTicks(-20);
//    }
//
//    public static void addKill(Player killer) {
//        ConfigurationManager.get(Config.PLAYERS).set(killer.getName() + ".stats.kills", getKills(killer) + 1);
//        ConfigurationManager.save(Config.PLAYERS);
//    }
//
//    public static void addDeath(Player victom) {
//        ConfigurationManager.get(Config.PLAYERS).set(victom.getName() + ".stats.deaths", getDeaths(victom) + 1);
//        ConfigurationManager.save(Config.PLAYERS);
//    }
//
//    public static int getKills(Player p) {
//        return deObfuscate(ConfigurationManager.get(Config.PLAYERS).getString(".stats.kills"));
//    }
//
//    public static int getDeaths(Player p) {
//        return deObfuscate(ConfigurationManager.get(Config.PLAYERS).getString(".stats.deaths"));
//    }
//
//    public double getKDRatio(Player p) {
//        return getKills(p) / getDeaths(p);
//    }
//
//    ////////////////////
//    //      Util      //
//    ////////////////////
//
//    private static final String obfuscate(int i) {
//        return Integer.toBinaryString(i).replace("0", "A").replace("1", "B");
//    }
//
//    private static final int deObfuscate(String i) {
//        return Integer.parseInt(i.replace("A", "0").replace("B", "1"), 2);
//    }
//
//}
