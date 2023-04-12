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

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * The asynchronous scheduler. The scheduler doubles as an {@link Executor} implementation
 * which simply forwards calls to {@link #run(Runnable)}
 *
 */
public interface AsynchronousScheduler extends SchedulerBase, Executor {

	/**
	 * Schedules a delayed task
	 *
	 * @param command what to run
	 * @param delay the delay
	 * @return the task
	 */
	ScheduledTask runDelayed(Runnable command, Duration delay);

	/**
	 * Schedules a delayed task
	 *
	 * @param command what to run
	 * @param delay the delay
	 */
	void runDelayed(Consumer<ScheduledTask> command, Duration delay);

	/**
	 * Schedules a repeating task
	 *
	 * @param command what to run
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 * @return the task
	 */
	ScheduledTask runAtFixedRate(Runnable command, Duration initialDelay, Duration period);

	/**
	 * Schedules a repeating task
	 *
	 * @param command what to run
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 */
	void runAtFixedRate(Consumer<ScheduledTask> command, Duration initialDelay, Duration period);

}
