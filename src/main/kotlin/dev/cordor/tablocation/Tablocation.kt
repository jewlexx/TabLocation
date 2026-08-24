package dev.cordor.tablocation

import com.jewelexx.craftcolours.CraftColours
import org.bukkit.plugin.java.JavaPlugin
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.World.Environment

enum class ShowDimension {
    Minimal,
    Expanded,
    False,
}

class Tablocation : JavaPlugin(), Listener {
    val log = Bukkit.getLogger()
    lateinit var environmentEnabled: ShowDimension
    var locationEnabled: Boolean = false
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
            Placeholders(this).register()
        }

        log.info("===================================")
        log.info("TabLocation has been enabled!")
        log.info("Version $version")
        log.info("Developed with 💗 by Juliette Cordor")
        log.info("===================================")

        UpdateChecker(this).getVersion { version ->
            if (description.version != version) {
                log.warning("[TabLocation] There is a new update available!")
            }
        }
    }

    override fun onDisable() {
        log.info("===================================")
        log.info("Plugin has been disabled!")
        log.info("Thank you for using TabLocation!")
        log.info("===================================")
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val fromBlock = e.from.block
        val toBlock = e.to?.block ?: return

        if (fromBlock.x != toBlock.x || fromBlock.y != toBlock.y || fromBlock.z != toBlock.z) {
            updateLocation(e.player)
        }
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        updateLocation(e.player)
    }

    fun updateLocation(player: Player) {
        player.setPlayerListName(withTeamName(player) + getLoc(player))
    }

    fun withTeamName(player: Player): String {
        val name = player.displayName
        val sb = Bukkit.getScoreboardManager()?.mainScoreboard ?: return name

        for (team in sb.teams) {
            if (team.hasEntry(player.name)) {
                return "${team.color}${team.prefix}$name${team.suffix}${CraftColours.RESET}"
            }
        }

        return name
    }

    fun getLoc(player: Player): String {
        if ((!locationEnabled && environmentEnabled == ShowDimension.False)
            || player.hasPermission("tablocation.hide")
        ) {
            return ""
        }

        var colourCode = CraftColours.WHITE

        var world = ""

        if (environmentEnabled != ShowDimension.False) {
            val environment = player.world.environment

            world = when (environment) {
                Environment.NORMAL -> "Overworld"
                Environment.NETHER -> "Nether"
                Environment.THE_END -> "End"
                else -> environment.toString()
            }

            val dimensionColourCode = config.getString("Colour for The $world") ?: ""

            if (environmentEnabled == ShowDimension.Expanded) {
                world = "${dimensionColourCode}The $world${CraftColours.RESET}"
            } else {
                colourCode = dimensionColourCode
                // Hide `world` variable if displaying minimal
                world = ""
            }
        }

        var location = ""

        if (locationEnabled) {
            val loc = player.location
            val x = loc.blockX
            val y = loc.blockY
            val z = loc.blockZ
            location = "$x, $y, $z"
        }

        var separator = ""

        if (locationEnabled && environmentEnabled == ShowDimension.Expanded) {
            separator = ", "
        }

        return " $colourCode[${CraftColours.WHITE}$location$separator$world$colourCode]"
    }
}
