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

/**
 * Supported and/or detectable adventure versions.
 * <p>
 * More versions may be added here in the future, but only in a fashion that is compatible with existing enum entries.
 * The versions in this enum are guaranteed to always be ordered in ascending order (higher ordinals, higher versions).
 */
public enum AdventureVersion {
    VER_4_7,
    VER_4_8,
    VER_4_9,
    VER_4_26,
    VER_5_1
}
