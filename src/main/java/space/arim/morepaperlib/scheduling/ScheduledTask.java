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

import org.bukkit.plugin.Plugin;

/**
 * A wrapper for either a standard BukkitTask or a Folia ScheduledTask
 */
public interface ScheduledTask {

	/**
	 * The plugin which created this task
	 *
	 * @return the owning plugin
	 */
	Plugin owningPlugin();

	/**
	 * Cancels the task
	 */
	void cancel();

	/**
	 * Determines whether the task was cancelled
	 *
	 * @return true if cancelled
	 */
	boolean isCancelled();

}
