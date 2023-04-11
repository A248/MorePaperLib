package space.arim.morepaperlib.adventure;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MorePaperLibAdventureTest {

    private MorePaperLibAdventure morePaperLibAdventure;

    @BeforeEach
    public void setMorePaperLibAdventure(@Mock Plugin plugin) {
        morePaperLibAdventure = new MorePaperLibAdventure(new MorePaperLib(plugin));
    }

    @Test
    public void kickPlayer(@Mock Player player) {
        Component message = Component.text("Kicked");
        morePaperLibAdventure.kickPlayer(player, message);
        verify(player).kick(message);
    }

    @ParameterizedTest
    @EnumSource(AsyncPlayerPreLoginEvent.Result.class)
    public void disallowPreLoginEvent(AsyncPlayerPreLoginEvent.Result result) throws UnknownHostException {
        Component message = Component.text("Denied login");
        InetAddress address = InetAddress.getByName("127.0.0.1");
        AsyncPlayerPreLoginEvent event = new AsyncPlayerPreLoginEvent(
                "A248", address, address, UUID.randomUUID(), mock(PlayerProfile.class));
        morePaperLibAdventure.disallowPreLoginEvent(event, result, message);
        assertSame(message, event.kickMessage());
        assertEquals(result, event.getLoginResult());
    }

}
