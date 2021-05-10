package space.arim.morepaperlib.it.spigot;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SpigotTest {

    @TempDir
    public File dataFolder;

    private CraftServer server;
    private MorePaperLib morePaperLib;

    @BeforeEach
    public void setup() {
        server = new CraftServer();

        PluginDescriptionFile descriptionFile = new PluginDescriptionFile("morepaperlib-it-spigot", "none", "none");
        @SuppressWarnings("deprecation")
        JavaPlugin plugin = new JavaPlugin(
                new JavaPluginLoader(server), descriptionFile, dataFolder, dataFolder) {};

        morePaperLib = new MorePaperLib(plugin);
    }

    @Test
    public void getCommandMap() {
        assertEquals(
                server.retrieveActualCommandMap(),
                morePaperLib.getServerCommandMap());
    }

    @Test
    public void getCommandMapKnownCommands() {
        assertEquals(
                server.retrieveActualKnownCommands(),
                morePaperLib.getCommandMapKnownCommands(server.retrieveActualCommandMap()));
    }
}
