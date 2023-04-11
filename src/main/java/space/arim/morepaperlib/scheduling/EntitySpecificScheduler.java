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

import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

final class EntitySpecificScheduler implements AttachedScheduler {

	private final EntityScheduler entityScheduler;
	private final Plugin plugin;

	EntitySpecificScheduler(Entity entity, Plugin plugin) {
		this.entityScheduler = entity.getScheduler();
		this.plugin = plugin;
	}

	@Override
	public @Nullable ScheduledTask run(Runnable command, Runnable alternateIfRemoved) {
		return FoliaTask.ofNullable(
				entityScheduler.run(plugin, (task) -> command.run(), alternateIfRemoved)
		);
	}

	@Override
	public boolean run(Consumer<ScheduledTask> command, Runnable alternateIfRemoved) {
		return entityScheduler.run(
				plugin, (task) -> command.accept(new FoliaTask(task)), alternateIfRemoved
		) != null;
	}

	@Override
	public @Nullable ScheduledTask runDelayed(Runnable command, Runnable alternateIfRemoved, long delay) {
		return FoliaTask.ofNullable(
				entityScheduler.runDelayed(plugin, (task) -> command.run(), alternateIfRemoved, delay)
		);
	}

	@Override
	public boolean runDelayed(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long delay) {
		return entityScheduler.runDelayed(
				plugin, (task) -> command.accept(new FoliaTask(task)), alternateIfRemoved, delay
		) != null;
	}

	@Override
	public @Nullable ScheduledTask runAtFixedRate(Runnable command, Runnable alternateIfRemoved, long initialDelay, long period) {
		return FoliaTask.ofNullable(
				entityScheduler.runAtFixedRate(
						plugin, (task) -> command.run(), alternateIfRemoved, initialDelay, period
				)
		);
	}

	@Override
	public boolean runAtFixedRate(Consumer<ScheduledTask> command, Runnable alternateIfRemoved, long initialDelay, long period) {
		return entityScheduler.runAtFixedRate(
				plugin, (task) -> command.accept(new FoliaTask(task)), alternateIfRemoved, initialDelay, period
		) != null;
	}

}
