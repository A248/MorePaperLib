/*
 * MorePaperLib
 * Copyright © 2026 Anand Beh
 *
 * MorePaperLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MorePaperLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MorePaperLib. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */

package space.arim.morepaperlib.adventure;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void textOfChildren() {
        ComponentLike[] children = new ComponentLike[] {Component.text("one"), Component.text("two")};
        Component ofChildren = assertDoesNotThrow(() -> morePaperLibAdventure.textOfChildren(children));
        assertEquals(List.of(children), ofChildren.children());
    }

    @Test
    public void componentToBuilder() {
        Component component = Component.text("compat", NamedTextColor.BLUE);
        ComponentBuilder<?, ?> builder = assertDoesNotThrow(() -> morePaperLibAdventure.componentToBuilder(component));
        assertNotNull(builder);
        Component built = builder.build();
        TextComponent builtText = assertInstanceOf(TextComponent.class, built);
        assertEquals("compat", builtText.content());
        assertEquals(NamedTextColor.BLUE, builtText.color());
    }

    @Test
    public void plainComponentSerializer() {
        assertEquals(
                "plain",
                morePaperLibAdventure.plainTextComponentSerializer().serialize(Component.text("plain", NamedTextColor.BLUE))
        );
        assertInstanceOf(PlainTextComponentSerializer.class, morePaperLibAdventure.plainTextComponentSerializer());
    }

    @Test
    public void joinComponents() {
        Component joined = assertDoesNotThrow(() ->
                morePaperLibAdventure.joinComponents(Component.text(","), Component.text("one"), Component.text("two")));
        assertEquals(
                "one,two", morePaperLibAdventure.plainTextComponentSerializer().serialize(joined)
        );
    }

    @ParameterizedTest
    @EnumSource(ClickEventType.class)
    public void clickEventCompat(ClickEventType eventType) {
        String value = eventType == ClickEventType.CHANGE_PAGE ? "1" : "clickme";
        ClickEvent clickEvent = morePaperLibAdventure.clickEvent(eventType, value);
        assertEquals(eventType, morePaperLibAdventure.clickEventType(clickEvent));
    }

    @Test
    public void clickEventActionCompat() {
        assertEquals(ClickEvent.Action.RUN_COMMAND, morePaperLibAdventure.clickEvent(ClickEventType.RUN_COMMAND, "banlist").action());
        assertEquals(ClickEvent.Action.SUGGEST_COMMAND, morePaperLibAdventure.clickEvent(ClickEventType.SUGGEST_COMMAND, "msg").action());
        assertEquals(ClickEvent.Action.OPEN_URL, morePaperLibAdventure.clickEvent(ClickEventType.OPEN_URL, "https://qassam.ps").action());
        assertEquals(ClickEvent.Action.OPEN_FILE, morePaperLibAdventure.clickEvent(ClickEventType.OPEN_FILE, "cmd.exe").action());
        assertEquals(ClickEvent.Action.COPY_TO_CLIPBOARD, morePaperLibAdventure.clickEvent(ClickEventType.COPY_TO_CLIPBOARD, "paste").action());
        assertEquals(ClickEvent.Action.CHANGE_PAGE, morePaperLibAdventure.clickEvent(ClickEventType.CHANGE_PAGE, "3").action());
    }
}
