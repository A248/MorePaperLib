package space.arim.morepaperlib;

import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.nio.file.Path;

import static org.mockito.Mockito.lenient;

public final class MockPlugin {

    private MockPlugin() { }

    public static void setServerLogger(Server server) {
        lenient().when(server.getLogger()).thenReturn(java.util.logging.Logger.getLogger(MockPlugin.class.getName()));
    }

    public static JavaPlugin create(Server server, Path dataFolder) {
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile("morepaperlib-it-spigot", "none", "none");
        File dataFolderFile = dataFolder.toFile();
        @SuppressWarnings("deprecation")
        JavaPlugin plugin = new JavaPlugin(
                new JavaPluginLoader(server), descriptionFile, dataFolderFile, dataFolderFile) {};
        return plugin;
    }
}
