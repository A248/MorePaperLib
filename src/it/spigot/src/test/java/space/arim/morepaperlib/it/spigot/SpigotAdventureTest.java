package space.arim.morepaperlib.it.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MockPlugin;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.morepaperlib.adventure.MorePaperLibAdventure;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpigotAdventureTest {

    private MorePaperLibAdventure morePaperLibAdventure;

    private static final char SECTION = LegacyComponentSerializer.SECTION_CHAR;

    @BeforeEach
    public void setMorePaperLibAdventure(@Mock Server server, @TempDir Path dataFolder) {
        MorePaperLib morePaperLib = new MorePaperLib(MockPlugin.create(server, dataFolder));
        morePaperLibAdventure = new MorePaperLibAdventure(morePaperLib);
    }

    @Test
    public void kickPlayer(@Mock Player player) {
        Component message = Component.text("Kicked", NamedTextColor.RED);
        morePaperLibAdventure.kickPlayer(player, message);
        verify(player).kickPlayer(SECTION + "cKicked");
    }

    @ParameterizedTest
    @EnumSource(AsyncPlayerPreLoginEvent.Result.class)
    public void disallowPreLoginEvent(AsyncPlayerPreLoginEvent.Result result) throws UnknownHostException {
        Component message = Component.text("Denied login", NamedTextColor.GRAY);
        AsyncPlayerPreLoginEvent event = new AsyncPlayerPreLoginEvent(
                "A248", InetAddress.getByName("127.0.0.1"), UUID.randomUUID());
        morePaperLibAdventure.disallowPreLoginEvent(event, result, message);
        assertEquals(SECTION + "7Denied login", event.getKickMessage());
        assertEquals(result, event.getLoginResult());
    }
}
