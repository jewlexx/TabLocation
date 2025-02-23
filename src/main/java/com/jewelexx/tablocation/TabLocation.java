package com.jewelexx.tablocation;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.jewelexx.craftcolours.CraftColours;

enum ShowDimension {
    Minimal,
    Expanded,
    False,
}

public final class TabLocation extends JavaPlugin implements Listener {
    static final Logger log = Bukkit.getLogger();
    static ShowDimension environmentEnabled;
    static boolean locationEnabled;
    static boolean bracketColourEnabled;
    static FileConfiguration config;
    final String version = getDescription().getVersion();

    @Override
    public void onEnable() {
        new org.bstats.bukkit.Metrics(this, 9922);

        PluginManager manager = Bukkit.getPluginManager();

        saveDefaultConfig();

        config = getConfig();

        String showDimension = config.getString("Show dimension");

        if (showDimension == "minimal") {
            environmentEnabled = ShowDimension.Minimal;
        } else if (showDimension == "true") {
            environmentEnabled = ShowDimension.Expanded;
        } else {
            environmentEnabled = ShowDimension.False;
        }

        locationEnabled = config.getBoolean("Show location");
        bracketColourEnabled = config.getBoolean("Colour brackets");

        manager.registerEvents(this, this);

        if (manager.getPlugin("PlaceholderAPI") != null) {
            new Placeholders(this).register();
        }

        log.info("===================================");
        log.info("TabLocation has been enabled!");
        log.info("Version " + version);
        log.info("Developed with 💗 by Juliette Cordor");
        log.info("===================================");

        new UpdateChecker(this).getVersion(version -> {
            if (!getDescription().getVersion().equals(version)) {
                log.warning("[TabLocation] There is a new update available.");
            }
        });
    }

    @Override
    public void onDisable() {
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

    static String withTeamName(Player player) {
        String name = player.getDisplayName();
        Scoreboard sb = Bukkit.getScoreboardManager().getMainScoreboard();

        for (Team team : sb.getTeams()) {
            if (team.hasEntry(player.getName())) {
                return team.getColor() + team.getPrefix() + name + team.getSuffix() + CraftColours.RESET;
            }
        }

        return name;
    }

    static void updateLocation(Player player) {
        player.setPlayerListName(withTeamName(player) + getLoc(player));
    }

    protected static String getLoc(Player player) {
        if ((!locationEnabled && environmentEnabled == ShowDimension.False)
                || player.hasPermission("tablocation.hide")) {
            return "";
        }

        String colourcode = CraftColours.WHITE;

        String world = "";

        if (environmentEnabled != ShowDimension.False) {
            Environment environment = player.getWorld().getEnvironment();

            switch (environment) {
                case NORMAL:
                    world = "Overworld";
                    break;
                case NETHER:
                    world = "Nether";
                    break;
                case THE_END:
                    world = "End";
                    break;
                default:
                    world = environment.toString();
                    break;
            }

            colourcode = config.getString("Colour for The " + world);

            if (environmentEnabled == ShowDimension.Expanded) {
                world = colourcode + "The " + world + CraftColours.RESET;
            }
        }

        String location = "";

        if (locationEnabled) {
            Location loc = player.getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            location = x + ", " + y + ", " + z;
        }

        String separator = "";

        if (locationEnabled && environmentEnabled == ShowDimension.Expanded) {
            separator = ", ";
        }

        return " " + colourcode + "[" + CraftColours.WHITE + location + separator + world + colourcode + "]";
    }
}
