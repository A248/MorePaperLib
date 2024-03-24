/*
 * MorePaperLib
 * Copyright © 2024 Anand Beh
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

	/**
	 * The execution state of this task
	 *
	 * @return the execution state
	 */
	ExecutionState getExecutionState();

	/**
	 * The status of a scheduled task. Mimics Folia's execution state but is also informed by the BukkitScheduler's
	 * equally capable (though more messy) API in this regard.
	 */
	enum ExecutionState {
		/**
		 * The task is cancelled, and it is not running.
		 */
		CANCELLED,
		/**
		 * The task is currently executing, but future executions are cancelled and it will not run again.
		 */
		CANCELLED_RUNNING,
		/**
		 * The task has run itself to completion and is not repeating, so it will not run again.
		 */
		FINISHED,
		/**
		 * The task is currently not executing, but may begin execution in the future. That is to say, the task is
		 * queued for a run but not yet running, and could be cancelled before that.
		 */
		IDLE,
		/**
		 * The task is currently running
		 */
		RUNNING,

	}

}
