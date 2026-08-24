package dev.cordor.tablocation

import dev.cordor.tablocation.github.Converter
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.function.Consumer

class UpdateChecker(private val plugin: JavaPlugin, private val updateUrl: URL) {
    fun getVersion(consumer: Consumer<String?>) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, Runnable {
            try {
                val inputStream: String? = readStringFromURL()

                val latestTag = Converter.fromJsonString(inputStream)?.get(0)
                val name = latestTag?.name

                consumer.accept(name)
            } catch (exception: IOException) {
                this.plugin.logger.info("Cannot look for updates: " + exception.message)
            } catch (exception: URISyntaxException) {
                this.plugin.logger.info("Cannot look for updates: " + exception.message)
            }
        })
    }

    @Throws(IOException::class, URISyntaxException::class)
    fun readStringFromURL(): String? {
        Scanner(
            updateUrl.openStream(),
            StandardCharsets.UTF_8
        ).use { scanner ->
            scanner.useDelimiter("\\A")
            return if (scanner.hasNext()) scanner.next() else ""
        }
    }
}