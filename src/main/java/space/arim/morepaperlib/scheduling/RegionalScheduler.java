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


import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

/**
 * A generalized interface for scheduling tasks tied to a certain region, (including the
 * global region) but <b>not</b> dependent on a particular entity. <br>
 * <br>
 * On Bukkit, implementations simply redirect to the BukkitScheduler. On Folia, the
 * implementations differ based on how they are obtained from {@link GracefulScheduling}
 *
 */
public interface RegionalScheduler extends SchedulerBase {

	/**
	 * Schedules a delayed task
	 *
	 * @param command what to run
	 * @param delay the delay in ticks
	 * @return the task
	 */
	ScheduledTask runDelayed(Runnable command, long delay);

	/**
	 * Schedules a delayed task
	 *
	 * @param command what to run
	 * @param delay the delay in ticks
	 */
	void runDelayed(Consumer<ScheduledTask> command, long delay);

	/**
	 * Schedules a repeating task
	 *
	 * @param command what to run
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 * @return the task
	 */
	ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period);

	/**
	 * Schedules a repeating task
	 *
	 * @param command what to run
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 */
	void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period);

	/**
	 * Pretends this regional scheduler is an attached scheduler. The attached scheduler is not in
	 * fact attached to anything, and so the "alternative callback" will always be ignored. Moreover,
	 * scheduling methods will never return {@code null}.
	 *
	 * @return this scheduler transformed into an attached scheduler
	 */
	default AttachedScheduler asAttachedScheduler() {
		class Transformation implements AttachedScheduler {

			@Override
			public @Nullable ScheduledTask run(Runnable command, Runnable alternateIfRemoved) {
				return RegionalScheduler.this.run(command);
			}

			@Override
			public boolean run(Consumer<ScheduledTask> command, Runnable alternateIfRemoved) {
				RegionalScheduler.this.run(command);
				return true;
			}

			@Override
			public @Nullable ScheduledTask runDelayed(Runnable command, Runnable alternateIfRemoved, long delay) {
				return RegionalScheduler.this.runDelayed(command, delay);
			}

			@Override
			public boolean runDelayed(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long delay) {
				RegionalScheduler.this.runDelayed(command, delay);
				return true;
			}

			@Override
			public @Nullable ScheduledTask runAtFixedRate(Runnable command, Runnable alternateIfRemoved, long initialDelay, long period) {
				return RegionalScheduler.this.runAtFixedRate(command, initialDelay, period);
			}

			@Override
			public boolean runAtFixedRate(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long initialDelay, long period) {
				RegionalScheduler.this.runAtFixedRate(command, initialDelay, period);
				return true;
			}

		}
		return new Transformation();
	}

}
