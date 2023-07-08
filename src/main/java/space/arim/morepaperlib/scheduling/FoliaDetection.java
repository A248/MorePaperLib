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

package space.arim.morepaperlib.scheduling;

/**
 * Allows overriding the automatic detection of the presence of Folia
 *
 */
public interface FoliaDetection {

	/**
	 * Controls whether Folia APIs will be used in preference to BukkitScheduler
	 *
	 * @return true to use Folia APIs
	 */
	boolean isUsingFolia();

	/**
	 * Forcibly disables Folia API usage
	 *
	 * @return disabled folia detection
	 */
	static FoliaDetection disabled() {
		return () -> false;
	}

	/**
	 * Forcibly enables Folia API usage
	 *
	 * @return enabled folia detection
	 */
	static FoliaDetection enabled() {
		return () -> true;
	}

}
