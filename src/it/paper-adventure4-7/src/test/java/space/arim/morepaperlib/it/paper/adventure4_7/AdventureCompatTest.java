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

package space.arim.morepaperlib.it.paper.adventure4_7;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;
import space.arim.morepaperlib.adventure.MorePaperLibAdventure;
import space.arim.morepaperlib.adventure.AdventureVersion;
import space.arim.morepaperlib.adventure.ClickEventType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AdventureCompatTest {

    private MorePaperLibAdventure morePaperLibAdventure;

    @BeforeEach
    public void setMorePaperLibAdventure(@Mock Plugin plugin) {
        morePaperLibAdventure = new MorePaperLibAdventure(new MorePaperLib(plugin));
    }

    @Test
    public void adventureVersion() {
        assertEquals(AdventureVersion.VER_4_7, morePaperLibAdventure.adventureVersion());
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
        assertThrows(ClassNotFoundException.class, () -> Class.forName("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer"));
    }

    @ParameterizedTest
    @EnumSource(ClickEventType.class)
    public void clickEventCompat(ClickEventType eventType) {
        String value = "clickme";
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
