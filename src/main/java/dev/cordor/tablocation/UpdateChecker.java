package dev.cordor.tablocation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.cordor.tablocation.github.Converter;
import dev.cordor.tablocation.github.Tags;

public class UpdateChecker {
    private final JavaPlugin plugin;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    static String readStringFromURL(String requestURL) throws IOException, URISyntaxException {
        try (Scanner scanner = new Scanner(new URI(requestURL).toURL().openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                String inputStream = readStringFromURL(
                        "https://api.github.com/repos/jewlexx/TabLocation/tags");

                Tags latestTag = Converter.fromJsonString(inputStream)[0];
                String name = latestTag.getName();

                consumer.accept(name);
            } catch (IOException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            } catch (URISyntaxException exception) {
                this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}