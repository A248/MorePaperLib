package space.arim.morepaperlib.adventure;

import net.kyori.adventure.text.Component;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MockPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.nio.file.Path;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MorePaperLibAdventureTest {

    private MorePaperLibAdventure morePaperLibAdventure;

    @BeforeEach
    public void setMorePaperLibAdventure(@Mock Server server, @TempDir Path dataFolder) {
        MockPlugin.setServerLogger(server);
        MorePaperLib morePaperLib = new MorePaperLib(MockPlugin.create(server, dataFolder));
        morePaperLibAdventure = new MorePaperLibAdventure(morePaperLib);
    }

    @Test
    public void kickPlayer(@Mock Player player) {
        Component message = Component.text("Kicked");
        morePaperLibAdventure.kickPlayer(player, message);
        verify(player).kick(message);
    }
}
