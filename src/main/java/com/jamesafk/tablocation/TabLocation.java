package com.jamesafk.tablocation;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TabLocation extends JavaPlugin implements Listener {

    public static Logger log = Bukkit.getLogger();
    public static String ver;
    public static Permission hide = new Permission("tablocation.hide");
    public static String javaver = System.getProperty("java.version");
    public static FileConfiguration config;
    public static boolean enviroment = true;
    public static String colourcode;

    @Override
    public void onEnable() {
        // Plugin startup logic

        ver = this.getDescription().getVersion();

        /*
        if (Bukkit.getPluginManager().getPermission("tablocation.hide") == null) {
            Bukkit.getPluginManager().addPermission(hide);
        }
         */

        Bukkit.getPluginManager().registerEvents(this, this);

        saveDefaultConfig();
        saveConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        config.addDefault("Add dimension to location", true);
        config.addDefault("Colour for dimension", "§5");
        saveDefaultConfig();
        saveConfig();

        colourcode = config.getString("Colour for dimension");

        enviroment = config.getBoolean("Add dimension to location");

        log.info("===================================");
        log.info("Plugin has been enabled!");
        log.info("You are using §aTabLocation,");
        log.info("Version §6" + ver);
        log.info("Java version §6" + javaver);
        log.info("Developed by §aJamesAfk");
        log.info("===================================");

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
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission(hide)) {
            String world = String.valueOf(player.getWorld().getEnvironment());

            String[] worlds = world.toLowerCase().split("_");
            int i = 0;
            while (i < worlds.length) {
                world = worlds[i].substring(0, 1).toUpperCase() + worlds[i].substring(1);
                i = i + 1;
            }

            if (world.equalsIgnoreCase("normal")) {
                world = "Overworld";
            }
            world = ", " + colourcode + "The " + world + "§f";

            if (!enviroment) {
                world = "";
            }

            String location = " (" + player.getLocation().getBlockX()
                    + ", " + player.getLocation().getBlockY()
                    + ", " + player.getLocation().getBlockZ()
                    + world + ")";

            player.setPlayerListName(player.getDisplayName() + location);
        } else {
            if (!player.getPlayerListName().equals(player.getName())) {
                player.setPlayerListName(player.getName());
            }
        }
    }
}
