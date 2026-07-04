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

import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.checkerframework.checker.nullness.qual.Nullable;
import space.arim.morepaperlib.MorePaperLib;

/**
 * Additional extensions which depend on the adventure API.
 * <p>
 * <b>Usage</b>
 * <p>
 * This class is designed for dynamic dependency loading. It links directly to adventure APIs. Your plugin environment
 * might use Adventure built into the platform (Adventure 5.1.1 since Paper 26.2), Adventure 4.x on older versions, or
 * a bundled or downloaded library version. This class handles <b>all</b> these versions, from version 4.7.0 upward.
 * <p>
 * Adventure version 4.7.0 is that used by Paper 1.16 when the library was first added to the server API.
 *
 */
public final class MorePaperLibAdventure {

    private final MorePaperLib morePaperLib;

    private final boolean hasMethodKick;
    private final boolean hasMethodDisallow;

    private final boolean hasTextOfChildren;
    private final boolean hasComponentToBuilder;
    private final boolean hasPlainTextComponentSerializer;

    /**
     * Creates with the default settings
     * @param morePaperLib the more paper lib instance
     */
    public MorePaperLibAdventure(MorePaperLib morePaperLib) {
        this.morePaperLib = morePaperLib;
        hasMethodKick = morePaperLib.methodExists(Player.class, "kick", Component.class);
        hasMethodDisallow = morePaperLib.methodExists(
                AsyncPlayerPreLoginEvent.class, "disallow", AsyncPlayerPreLoginEvent.Result.class, Component.class);
        hasTextOfChildren = morePaperLib.methodExists(Component.class, "textOfChildren", ComponentLike[].class);
        hasComponentToBuilder = morePaperLib.methodExists(Component.class, "toBuilder");
        hasPlainTextComponentSerializer = morePaperLib.classExists("net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer");
    }

    /**
     * Gets the {@code MorePaperLib} used
     *
     * @return the morepaperlib object
     */
    public MorePaperLib getMorePaperLib() {
        return morePaperLib;
    }

    private boolean isAdventure5() {
        return !ClickEvent.Action.class.isEnum();
    }

    /**
     * Detects the supported version of adventure that is being implemented
     *
     * @return the adventure version
     */
    public AdventureVersion adventureVersion() {
        if (isAdventure5()) {
            return AdventureVersion.VER_5_1;
        }
        if (hasComponentToBuilder) {
            return AdventureVersion.VER_4_26;
        }
        if (hasPlainTextComponentSerializer) {
            return AdventureVersion.VER_4_8;
        }
        return AdventureVersion.VER_4_7;
    }

    /**
     * Creates a component from children components.
     * <p>
     * Uses {@code Component.textOfChildren} or falls back to {@code TextComponent.ofChildren}.
     *
     * @param components the child components
     * @return the resulting component
     */
    @SuppressWarnings("deprecation")
    public Component textOfChildren(ComponentLike...components) {
        if (hasTextOfChildren) {
            return Component.textOfChildren(components);
        } else {
            //noinspection UnstableApiUsage
            return TextComponent.ofChildren(components);
        }
    }

    /**
     * Turns a component into its component builder.
     * <p>
     * Calls {@code component.toBuilder()} if possible, otherwise falls back to {@code BuildableComponent} on older
     * adventure versions.
     *
     * @param component the component
     * @return the component builder, or {@code null} if an older adventure version is being used and the component is not buildable
     */
    public @Nullable ComponentBuilder<?, ?> componentToBuilder(Component component) {
        if (hasComponentToBuilder) {
            return component.toBuilder();
        }
        // Be future-proof by not referencing BuildableComponent inside this method
        return deprecatedToBuilder(component);
    }

    @SuppressWarnings("deprecation")
    private static @Nullable ComponentBuilder<?, ?> deprecatedToBuilder(Component component) {
        if (component instanceof BuildableComponent<?, ?>) {
            return ((BuildableComponent<?, ?>) component).toBuilder();
        }
        return null;
    }

    /**
     * Gets the plain text component serializer.
     * <p>
     * Uses {@code PlainTextComponentSerializer} if available, otherwise the old version ({@code PlainComponentSerializer}).
     *
     * @return plain text component serializer
     */
    @SuppressWarnings("deprecation")
    public ComponentSerializer<Component, TextComponent, String> plainTextComponentSerializer() {
        if (hasPlainTextComponentSerializer) {
            return PlainTextComponentSerializer.plainText();
        } else {
            //noinspection UnstableApiUsage
            return PlainComponentSerializer.plain();
        }
    }

    /**
     * Simulates the factory method {@code ClickEvent.clickEvent(Action, String)} which was removed in Adventure 5.0.
     *
     * @param type the click event action
     * @param value the click event value
     * @return the click event
     * @throws IllegalArgumentException if we are running Adventure 4.22 or later, {@code type} is {@code CHANGE_PAGE}
     * but the value argument cannot be parsed as an integer
     */
    public ClickEvent clickEvent(ClickEventType type, String value) {
        switch (type) {
            case OPEN_URL:
                return ClickEvent.openUrl(value);
            case OPEN_FILE:
                return ClickEvent.openFile(value);
            case RUN_COMMAND:
                return ClickEvent.runCommand(value);
            case SUGGEST_COMMAND:
                return ClickEvent.suggestCommand(value);
            case CHANGE_PAGE:
                if (isAdventure5()) {
                    return ClickEvent.changePage(Integer.parseInt(value));
                } else {
                    return changePageStr(value);
                }
            case COPY_TO_CLIPBOARD:
                return ClickEvent.copyToClipboard(value);
            default:
                throw new IncompatibleClassChangeError("Unknown " + type);
        }
    }

    @SuppressWarnings("deprecation")
    private ClickEvent changePageStr(String value) {
        return ClickEvent.changePage(value);
    }

    /**
     * Gets the action out of a click event, limiting the actions to those available in Adventure 4.7.
     *
     * @param clickEvent the click event
     * @return the click event action, or {@code null} if the action type does not exist in Adventure 4.7
     */
    public @Nullable ClickEventType clickEventType(ClickEvent clickEvent) {
        ClickEvent.Action action = clickEvent.action();
        if (isAdventure5()) {
            return ClickEventType.fetchAdventure5(action);
        } else {
            return ClickEventType.fetchAdventure4(action);
        }
    }

    /**
     * Kicks a player with the given reason. If the {@link Player#kick(Component)}
     * method does not exist, legacy formatting is used.
     *
     * @param player the player to kick
     * @param message the kick essage
     */
    public void kickPlayer(Player player, Component message) {
        if (hasMethodKick) {
            player.kick(message);
        } else {
            //noinspection deprecation
            player.kickPlayer(LegacyComponentSerializer.legacySection().serialize(message));
        }
    }

    /**
     * Disallows the login event with the given result and reason. If the
     * {@link AsyncPlayerPreLoginEvent#disallow(AsyncPlayerPreLoginEvent.Result, Component)} method does not exist,
     * legacy formatting is used.
     *
     * @param event the event
     * @param result the event result to set
     * @param message the denial message
     */
    public void disallowPreLoginEvent(AsyncPlayerPreLoginEvent event,
                                      AsyncPlayerPreLoginEvent.Result result, Component message) {
        if (hasMethodDisallow) {
            event.disallow(result, message);
        } else {
            //noinspection deprecation
            event.disallow(result, LegacyComponentSerializer.legacySection().serialize(message));
        }
    }
}
