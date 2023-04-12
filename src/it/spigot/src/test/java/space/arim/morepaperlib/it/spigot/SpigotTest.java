package space.arim.morepaperlib.it.spigot;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpigotTest {

    private CraftServer server;
    private MorePaperLib morePaperLib;

    @BeforeEach
    public void setup(@Mock Plugin plugin) {
        server = new CraftServer();
        morePaperLib = new MorePaperLib(plugin);
    }

    @Test
    public void getCommandMap() {
        when(morePaperLib.getPlugin().getServer()).thenReturn(server);
        assertEquals(
                server.retrieveActualCommandMap(),
                morePaperLib.commandRegistration().getServerCommandMap());
    }

    @Test
    public void getCommandMapKnownCommands() {
        assertEquals(
                server.retrieveActualKnownCommands(),
                morePaperLib.commandRegistration().getCommandMapKnownCommands(server.retrieveActualCommandMap()));
    }
}
