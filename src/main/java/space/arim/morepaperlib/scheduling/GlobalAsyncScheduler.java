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

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

final class GlobalAsyncScheduler implements AsynchronousScheduler {

	private final AsyncScheduler asyncScheduler;
	private final Plugin plugin;

	GlobalAsyncScheduler(Plugin plugin) {
		this.asyncScheduler = plugin.getServer().getAsyncScheduler();
		this.plugin = plugin;
	}

	void cancelTasks() {
		asyncScheduler.cancelTasks(plugin);
	}

	@Override
	public void execute(@NotNull Runnable command) {
		asyncScheduler.runNow(plugin, (task) -> command.run());
	}

	@Override
	public ScheduledTask run(Runnable command) {
		return new FoliaTask(
				asyncScheduler.runNow(plugin, (task) -> command.run())
		);
	}

	@Override
	public void run(Consumer<ScheduledTask> command) {
		asyncScheduler.runNow(plugin, (task) -> command.accept(new FoliaTask(task)));
	}

	@Override
	public ScheduledTask runDelayed(Runnable command, Duration delay) {
		return new FoliaTask(
				asyncScheduler.runDelayed(plugin, (task) -> command.run(), delay.toNanos(), TimeUnit.NANOSECONDS)
		);
	}

	@Override
	public void runDelayed(Consumer<ScheduledTask> command, Duration delay) {
		asyncScheduler.runDelayed(
				plugin, (task) -> command.accept(new FoliaTask(task)), delay.toNanos(), TimeUnit.NANOSECONDS
		);
	}

	@Override
	public ScheduledTask runAtFixedRate(Runnable command, Duration initialDelay, Duration period) {
		return new FoliaTask(
				asyncScheduler.runAtFixedRate(
						plugin, (task) -> command.run(), initialDelay.toNanos(), period.toNanos(), TimeUnit.NANOSECONDS
				)
		);
	}

	@Override
	public void runAtFixedRate(Consumer<ScheduledTask> command, Duration initialDelay, Duration period) {
		asyncScheduler.runAtFixedRate(
				plugin, (task) -> command.accept(new FoliaTask(task)), initialDelay.toNanos(), period.toNanos(), TimeUnit.NANOSECONDS
		);
	}

}
