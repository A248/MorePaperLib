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

import net.kyori.adventure.text.event.ClickEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Compatibility enum for Adventure 4.x's ClickEvent.Action enum
 */
public enum ClickEventType {
    // Important: enum names and order must match exactly (used to fetch original enum)
    OPEN_URL,
    OPEN_FILE,
    RUN_COMMAND,
    SUGGEST_COMMAND,
    CHANGE_PAGE,
    COPY_TO_CLIPBOARD
    ;

    private final ClickEvent.Action action;

    ClickEventType() {
        Class<?> clazz = ClickEvent.Action.class;
        ClickEvent.Action action;
        try {
            java.lang.reflect.Field field = clazz.getDeclaredField(name());
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new IncompatibleClassChangeError("Not static: " + name() + " in " + clazz);
            }
            action = (ClickEvent.Action) Objects.requireNonNull(field.get(null), "null action " + name());
        } catch (NoSuchFieldException | IllegalAccessException | ClassCastException ex) {
            IncompatibleClassChangeError icce = new IncompatibleClassChangeError("Bad or unknown field: " + name() + " in " + clazz);
            icce.initCause(ex);
            throw icce;
        }
        this.action = action;
    }

    static ClickEventType fetchAdventure4(ClickEvent.Action action) {
        ClickEventType[] values = values();
        int ordinal = action.ordinal();
        if (ordinal < values.length) {
            ClickEventType ret = values[ordinal];
            assert ret.action.equals(action) : "action mismatch";
            return ret;
        }
        return null;
    }

    static @Nullable ClickEventType fetchAdventure5(ClickEvent.Action action) {
        for (ClickEventType type : values()) {
            if (type.action.equals(action)) {
                return type;
            }
        }
        return null;
    }
}
