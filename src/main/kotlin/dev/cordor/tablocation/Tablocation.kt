package dev.cordor.tablocation

import com.jewelexx.craftcolours.CraftColours
import io.papermc.paper.ServerBuildInfo
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.World.Environment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import java.net.URI


enum class ShowDimension {
    Minimal,
    Expanded,
    False,
}

class Tablocation : JavaPlugin(), Listener {
    lateinit var environmentEnabled: ShowDimension
    var locationEnabled: Boolean = false
    val version = description.version

    companion object {
        fun isFolia(): Boolean {
            return ServerBuildInfo.buildInfo().isBrandCompatible(Key.key("papermc", "folia"))
        }
    }

    override fun onEnable() {
        org.bstats.bukkit.Metrics(this, 9922)

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
        locationEnabled = config.getBoolean("Show location")

        val manager = server.pluginManager

        manager.registerEvents(this, this)
        if (manager.getPlugin("PlaceholderAPI") != null) {
            Placeholders(this).register()
        }

        logger.info("===================================")
        logger.info("TabLocation has been enabled!")
        logger.info("Version $version")
        logger.info("Developed with 💗 by Juliette Cordor")
        logger.info("===================================")

        UpdateChecker(this, URI("https://api.github.com/repos/jewlexx/TabLocation/tags").toURL()).getVersion { version ->
            if (description.version != version) {
                logger.warning("[TabLocation] There is a new update available!")
            }
        }
    }

    override fun onDisable() {
        logger.info("===================================")
        logger.info("Plugin has been disabled!")
        logger.info("Thank you for using TabLocation!")
        logger.info("===================================")
    }

    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        val fromBlock = e.from.block
        val toBlock = e.to.block ?: return

        if (fromBlock.x != toBlock.x || fromBlock.y != toBlock.y || fromBlock.z != toBlock.z) {
            updateLocation(e.player)
        }
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        updateLocation(e.player)
    }

    fun updateLocation(player: Player) {
        val newName = player.playerListName();
        newName.append(Component.text(getLoc(player)));
        player.playerListName(newName)
    }

//    fun withTeamName(player: Player): String {
//        val name = player.displayName()
//        val sb = server.scoreboardManager.mainScoreboard
//
//        for (team in sb.teams) {
//            if (team.hasEntry(player.name)) {
//                val nameComponent = Component.empty()
//                nameComponent.append(team.name)
//                return "${team.color}${team.prefix}$name${team.suffix}${CraftColours.RESET}"
//            }
//        }
//
//        return name
//    }

    fun getLoc(player: Player): String {
        if ((!locationEnabled && environmentEnabled == ShowDimension.False)
            || player.hasPermission("tablocation.hide")
        ) {
            return ""
        }

        var colourCode = CraftColours.WHITE

        val world = if (environmentEnabled != ShowDimension.False) {
            val environmentName = when (val environment = player.world.environment) {
                Environment.NORMAL -> "Overworld"
                Environment.NETHER -> "Nether"
                Environment.THE_END -> "End"
                else -> environment.toString()
            }

            val dimensionColourCode = config.getString("Colour for The $environmentName") ?: ""

            if (environmentEnabled == ShowDimension.Expanded) {
                "${dimensionColourCode}The $environmentName${CraftColours.RESET}"
            } else {
                colourCode = dimensionColourCode
                // Hide `world` variable if displaying minimal
                ""
            }
        } else {
            ""
        }

        val location = if (locationEnabled) {
            val loc = player.location
            val x = loc.blockX
            val y = loc.blockY
            val z = loc.blockZ
            "$x, $y, $z"
        } else {
            ""
        }

        val separator = if (locationEnabled && environmentEnabled == ShowDimension.Expanded) {
            ", "
        } else {
            ""
        }

        return " $colourCode[${CraftColours.WHITE}$location$separator$world$colourCode]"
    }
}
