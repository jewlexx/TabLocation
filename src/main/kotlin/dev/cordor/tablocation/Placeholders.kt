package dev.cordor.tablocation

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
class Placeholders
/**
 * Since we register the expansion inside our own plugin, we
 * can simply use this method here to get an instance of our
 * plugin.
 * 
 * @param plugin
 * The instance of our plugin.
 */(private val plugin: JavaPlugin) : PlaceholderExpansion() {
    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister
     * your expansion class when
     * PlaceholderAPI is reloaded
     * 
     * @return true to persist through reloads
     */
    override fun persist(): Boolean {
        return true
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return `true`
     * 
     * @return Always true since it's an internal class.
     */
    override fun canRegister(): Boolean {
        return true
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br></br>
     * For convienience do we return the author from the plugin.yml
     * 
     * @return The name of the author as a String.
     */
    override fun getAuthor(): String {
        return plugin.getDescription().getAuthors().toString()
    }

    /**
     * The placeholder identifier should go here.
     * <br></br>
     * This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br></br>
     * The identifier has to be lowercase and can't contain _ or %
     * 
     * @return The identifier in `%<identifier>_<value>%` as String.
     */
    override fun getIdentifier(): String {
        return "tablocation"
    }

    /**
     * This is the version of the expansion.
     * <br></br>
     * You don't have to use numbers, since it is set as a String.
     * 
     * For convienience do we return the version from the plugin.yml
     * 
     * @return The version as a String.
     */
    override fun getVersion(): String {
        return plugin.getDescription().getVersion()
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br></br>
     * We specify the value identifier in this method.
     * <br></br>
     * Since version 2.9.1 can you use OfflinePlayers in your requests.
     * 
     * @param player
     * A [Player][Player].
     * @param identifier
     * A String containing the identifier/value.
     * 
     * @return possibly-null String of the requested identifier.
     */
    override fun onPlaceholderRequest(player: Player?, identifier: String): String? {
        if (player == null) {
            return ""
        }

        if (identifier == "location") {
            return plugin.getConfig().getString("location", TabLocation.getLoc(player))
        }

        return null
    }
}