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

import java.util.function.Consumer;

/**
 * Base interface for all schedulers
 *
 */
public interface SchedulerBase {

	/**
	 * Schedules a task
	 *
	 * @param command what to run
	 * @return the task
	 */
	ScheduledTask run(Runnable command);

	/**
	 * Schedules a task
	 *
	 * @param command what to run
	 */
	void run(Consumer<ScheduledTask> command);

}
