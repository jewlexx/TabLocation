package dev.cordor.tablocation

import org.bukkit.plugin.java.JavaPlugin
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.Listener

enum class ShowDimension {
    Minimal,
    Expanded,
    False,
}

class Tablocation : JavaPlugin(), Listener  {
    companion object {
        val log = Bukkit.getLogger()
    }
    lateinit var environmentEnabled: ShowDimension
    var locationEnabled: Boolean? = null
    val version = description.version


    override fun onEnable() {
        Metrics(this, 9922)

        saveDefaultConfig()
        val showDimension = config.getString("Show dimension")

        environmentEnabled = when (showDimension) {
            "minimal" -> {
                ShowDimension.Minimal
            }

            "expanded" -> {
                ShowDimension.Expanded
            }

            else -> {
                ShowDimension.False
            }
        }
        locationEnabled = config.getBoolean("Location")

        val manager = Bukkit.getPluginManager()

        manager.registerEvents(this, this)
        if (manager.getPlugin("PlaceholderAPI") != null) {
            // TODO: implement placeholders
        }

        log.info("===================================");
        log.info("TabLocation has been enabled!");
        log.info("Version $version");
        log.info("Developed with 💗 by Juliette Cordor");
        log.info("===================================");

        // TODO: update checker
    }

    override fun onDisable() {
        log.info("===================================");
        log.info("Plugin has been disabled!");
        log.info("Thank you for using TabLocation!");
        log.info("===================================");
    }
}
