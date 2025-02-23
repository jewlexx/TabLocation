package com.jewelexx.tablocation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TabLocation extends JavaPlugin implements Listener {
    static String javaver = System.getProperty("java.version");
    static Logger log = Bukkit.getLogger();
    static PluginManager manager = Bukkit.getPluginManager();
    static String ver;
    static boolean environment;
    static boolean locationBool;
    static FileConfiguration config;

    @Override
    public void onEnable() {
        new org.bstats.bukkit.Metrics(this, 9922);

        // Plugin startup logic

        ver = getDescription().getVersion();

        config = getConfig();

        config.options().copyDefaults(true);
        config.addDefault("Show dimension", true);
        config.addDefault("Show location", true);
        config.addDefault("Colour for The Overworld", "§a");
        config.addDefault("Colour for The Nether", "§4");
        config.addDefault("Colour for The End", "§5");
        saveConfig();

        environment = config.getBoolean("Show dimension");
        locationBool = config.getBoolean("Show location");

        manager.registerEvents(this, this);

        if (manager.getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        log.info("===================================");
        log.info("TabLocation has been enabled!");
        log.info("Version " + ver);
        log.info("Java version " + javaver);
        log.info("Developed with 💗 by Juliette Cordor");
        log.info("===================================");

        new UpdateChecker(this, 83894).getVersion(version -> {
            if (getDescription().getVersion() != version) {
                log.warning("[TabLocation] There is a new update available.");
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        log.info("===================================");
        log.info("Plugin has been disabled!");
        log.info("Thank you for using TabLocation!");
        log.info("===================================");

    }

    @EventHandler
    static void onPlayerMove(PlayerMoveEvent e) {
        Block from = e.getFrom().getBlock();
        Block to = e.getTo().getBlock();
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            updateLocation(e.getPlayer());
        }
    }

    @EventHandler
    static void onPlayerJoin(PlayerJoinEvent e) {
        updateLocation(e.getPlayer());
    }

    static void updateLocation(Player player) {
        player.setPlayerListName(player.getDisplayName() + getLoc(player));
    }

    protected static String getLoc(Player player) {
        if ((!locationBool && !environment) || player.hasPermission("tablocation.hide")) {
            return "";
        }

        String world = "";

        if (environment) {
            /**
             * The environment is formatted as follows:
             * - Overworld
             * - Nether
             * - The_End
             * 
             * And in order to convert it into a string and remove the underscore in
             * "The_End" we have this mess.
             */
            String[] split = player.getWorld().getEnvironment().name().toLowerCase().split("_");
            String s = split[split.length - 1];

            if (s.equals("normal")) {
                s = "Overworld";
            }

            world = s.substring(0, 1).toUpperCase() + s.substring(1);

            log.info("Colour for The " + world);
            String colourcode = config.getString("Colour for The " + world);

            world = String.format("%sThe %s§f", colourcode, world);
        }

        String location = "";

        if (locationBool) {
            Location loc = player.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            location = String.format("%s, %s, %s", x, y, z);
        }

        String separator = "";

        if (locationBool && environment) {
            separator = ", ";
        }

        String tabLoc = " [" + location + separator + world + "]";

        return tabLoc;
    }
}
