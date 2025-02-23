package com.jewelexx.tablocation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import com.jewelexx.tablocation.Other.Metrics;
import com.jewelexx.tablocation.Other.Placeholders;
import com.jewelexx.tablocation.Other.UpdateChecker;

public final class TabLocation extends JavaPlugin implements Listener {

    public static Logger log = Bukkit.getLogger();
    public static String ver;
    public static String hide = "tablocation.hide";
    public static String javaver = System.getProperty("java.version");
    public static FileConfiguration config;
    public static boolean enviroment = true;
    public static boolean locationBool = true;
    public static String colourcode;

    @Override
    public void onEnable() {
        int pluginId = 9922;
        new Metrics(this, pluginId);

        // Plugin startup logic

        ver = this.getDescription().getVersion();

        Bukkit.getPluginManager().registerEvents(this, this);

        saveConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        config.addDefault("Show dimension", true);
        config.addDefault("Show location", true);
        config.addDefault("Colour for dimension", "§5");
        saveDefaultConfig();
        saveConfig();

        colourcode = config.getString("Colour for dimension");

        enviroment = config.getBoolean("Show dimension");
        locationBool = config.getBoolean("Show location");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        log.info("===================================");
        log.info("TabLocation has been enabled!");
        log.info("Version " + ChatColor.GOLD + ver);
        log.info("Java version " + ChatColor.GOLD + javaver);
        log.info("Developed with 💗 by Juliette Cordor");
        log.info("===================================");

        new UpdateChecker(this, 83894).getVersion(version -> {
            if (this.getDescription().getVersion() != version) {
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
    public void onPlayerMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) {
            Player player = e.getPlayer();
            player.setPlayerListName(player.getDisplayName() + getLoc(player));
        }
    }

    public static String getLoc(Player player) {
        if (!player.hasPermission(hide)) {
            String world = "";

            for (String s : String.valueOf(player.getWorld().getEnvironment()).toLowerCase().split("_")) {
                if (s.equals("normal")) {
                    s = "Overworld";
                }
                world = s.substring(0, 1).toUpperCase() + s.substring(1);
            }

            if (world.equalsIgnoreCase("normal")) {
                world = "Overworld";
            }
            world = colourcode + "The " + world + "§f";

            String location = player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", "
                    + player.getLocation().getBlockZ();

            if (!enviroment) {
                world = "";
            }

            if (!locationBool) {
                location = "";
            }

            String separator = ", ";

            if ((!locationBool && enviroment) || (!enviroment && locationBool)) {
                separator = "";
            }

            String tabLoc = " [" + location + separator + world + "]";

            if (!locationBool && !enviroment) {
                tabLoc = "";
            }

            return tabLoc;
        } else {
            if (!player.getPlayerListName().equals(player.getName())) {
                return "";
            }
        }
        return "";
    }
}
