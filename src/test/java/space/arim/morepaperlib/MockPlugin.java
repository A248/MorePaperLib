package space.arim.morepaperlib;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.nio.file.Path;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public final class MockPlugin {

    private MockPlugin() { }

    public static Plugin create(Server server, Path dataFolder) {
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile("morepaperlib-it-spigot", "none", "none");

        Plugin plugin = mock(Plugin.class);
        lenient().when(plugin.getDataFolder()).thenReturn(dataFolder.toFile());
        lenient().when(plugin.getServer()).thenReturn(server);
        lenient().when(plugin.getDescription()).thenReturn(descriptionFile);
        return plugin;
    }

}
