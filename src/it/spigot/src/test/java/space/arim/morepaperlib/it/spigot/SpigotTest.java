package space.arim.morepaperlib.it.spigot;

import org.bukkit.craftbukkit.CraftServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MockPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SpigotTest {

    @TempDir
    public Path dataFolder;

    private CraftServer server;
    private MorePaperLib morePaperLib;

    @BeforeEach
    public void setup() {
        server = new CraftServer();
        morePaperLib = new MorePaperLib(MockPlugin.create(server, dataFolder));
    }

    @Test
    public void getCommandMap() {
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
