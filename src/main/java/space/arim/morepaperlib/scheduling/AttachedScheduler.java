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
 * A scheduler attached to an entity or other disappearable object. The scheduler becomes
 * "detached" when the entity is removed. <br>
 * <br>
 * When a task is submitted, if the underlying entity has already been removed, the method yields
 * {@code null} or {@code false} (depending on whether the Runnable or Consumer parameter is
 * selected). Otherwise, the task is scheduled. If the entity still exists when it comes time to
 * run the task, the task is run; if the entity has ceased to exist within this time, the alternate
 * callback is run. <br>
 * <br>
 * If the implementation never becomes detached, this might be because it is either the BukkitScheduler
 * or a wrapped {@link RegionalScheduler#asAttachedScheduler()} in disguise.
 */
public interface AttachedScheduler {

	/**
	 * Schedules a task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @return the task if scheduled, or {@code null} if the entity is known to be already removed
	 */
	@Nullable ScheduledTask run(Runnable command, Runnable alternateIfRemoved);

	/**
	 * Schedules a task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @return true if scheduled, false if the entity is known to be already removed
	 */
	boolean run(Consumer<ScheduledTask> command, Runnable alternateIfRemoved);

	/**
	 * Schedules a delayed task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @param delay the delay in ticks
	 * @return the task if scheduled, or {@code null} if the entity is known to be already removed
	 */
	@Nullable ScheduledTask runDelayed(Runnable command, Runnable alternateIfRemoved, long delay);

	/**
	 * Schedules a delayed task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @param delay the delay in ticks
	 * @return true if scheduled, false if the entity is known to be already removed
	 */
	boolean runDelayed(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long delay);

	/**
	 * Schedules a repeating task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 * @return the task if scheduled, or {@code null} if the entity is known to be already removed
	 */
	@Nullable ScheduledTask runAtFixedRate(Runnable command, Runnable alternateIfRemoved, long initialDelay, long period);

	/**
	 * Schedules a repeating task to run where the entity exists
	 *
	 * @param command what to run
	 * @param alternateIfRemoved what to run if the entity has been removed
	 * @param initialDelay the initial delay in ticks
	 * @param period the period in ticks
	 * @return true if scheduled, false if the entity is known to be already removed
	 */
	boolean runAtFixedRate(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long initialDelay, long period);

}
