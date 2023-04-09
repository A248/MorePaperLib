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
import org.checkerframework.checker.nullness.qual.Nullable;

class FoliaTask implements ScheduledTask {

	private final io.papermc.paper.threadedregions.scheduler.ScheduledTask task;

	FoliaTask(io.papermc.paper.threadedregions.scheduler.ScheduledTask task) {
		this.task = task;
	}

	static FoliaTask ofNullable(io.papermc.paper.threadedregions.scheduler.@Nullable ScheduledTask task) {
		return (task == null) ? null : new FoliaTask(task);
	}

	@Override
	public Plugin owningPlugin() {
		return task.getOwningPlugin();
	}

	@Override
	public void cancel() {
		task.cancel();
	}

	@Override
	public boolean isCancelled() {
		return task.isCancelled();
	}

}
