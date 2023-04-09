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

import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

final class GlobalScheduler implements RegionalScheduler {

	private final GlobalRegionScheduler globalRegionScheduler;
	private final Plugin plugin;

	GlobalScheduler(GlobalRegionScheduler globalRegionScheduler, Plugin plugin) {
		this.globalRegionScheduler = globalRegionScheduler;
		this.plugin = plugin;
	}

	@Override
	public ScheduledTask run(Runnable command) {
		return new FoliaTask(
				globalRegionScheduler.run(plugin, (task) -> command.run())
		);
	}

	@Override
	public void run(Consumer<ScheduledTask> command) {
		globalRegionScheduler.run(plugin, (task) -> command.accept(new FoliaTask(task)));
	}

	@Override
	public ScheduledTask runDelayed(Runnable command, long delay) {
		return new FoliaTask(
				globalRegionScheduler.runDelayed(plugin, (task) -> command.run(), delay)
		);
	}

	@Override
	public void runDelayed(Consumer<ScheduledTask> command, long delay) {
		globalRegionScheduler.runDelayed(plugin, (task) -> command.accept(new FoliaTask(task)), delay);
	}

	@Override
	public ScheduledTask runAtFixedRate(Runnable command, long initialDelay, long period) {
		return new FoliaTask(
				globalRegionScheduler.runAtFixedRate(plugin, (task) -> command.run(), initialDelay, period)
		);
	}

	@Override
	public void runAtFixedRate(Consumer<ScheduledTask> command, long initialDelay, long period) {
		globalRegionScheduler.runAtFixedRate(plugin, (task) -> command.accept(new FoliaTask(task)), initialDelay, period);
	}

}
