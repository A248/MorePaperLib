/*
 * MorePaperLib
 * Copyright © 2023 Anand Beh
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

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import space.arim.morepaperlib.MorePaperLib;

/**
 * Additional extensions which depend on the adventure API. <br>
 * <br>
 * All public methods are implemented independently of each other.
 *
 *
 */
public final class MorePaperLibAdventure {

    private final MorePaperLib morePaperLib;

    private final boolean hasMethodKick;
    private final boolean hasMethodDisallow;

    public MorePaperLibAdventure(MorePaperLib morePaperLib) {
        this.morePaperLib = morePaperLib;
        hasMethodKick = morePaperLib.methodExists(Player.class, "kick", Component.class);
        hasMethodDisallow = morePaperLib.methodExists(
                AsyncPlayerPreLoginEvent.class, "disallow", AsyncPlayerPreLoginEvent.Result.class, Component.class);
    }

    /**
     * Gets the {@code MorePaperLib} used
     *
     * @return the morepaperlib object
     */
    public MorePaperLib getMorePaperLib() {
        return morePaperLib;
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
