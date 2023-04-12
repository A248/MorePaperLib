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
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import space.arim.morepaperlib.MorePaperLib;

import java.time.Duration;
import java.util.function.Consumer;

@SuppressWarnings("deprecation")
final class BukkitSchedulerAsAsynchronousScheduler implements AsynchronousScheduler {

	private final BukkitScheduler scheduler;
	private final Plugin plugin;

	private final boolean hasMethodRunTask;
	private final boolean hasMethodRunTaskLater;
	private final boolean hasMethodRunTaskTimer;

	BukkitSchedulerAsAsynchronousScheduler(MorePaperLib morePaperLib) {
		Plugin plugin = morePaperLib.getPlugin();
		this.scheduler = plugin.getServer().getScheduler();
		this.plugin = plugin;
		hasMethodRunTask = morePaperLib.methodExists(
				BukkitScheduler.class, "runTaskAsynchronously", Plugin.class, Consumer.class
		);
		hasMethodRunTaskLater = morePaperLib.methodExists(
				BukkitScheduler.class, "runTaskLaterAsynchronously", Plugin.class, Consumer.class, long.class
		);
		hasMethodRunTaskTimer = morePaperLib.methodExists(
				BukkitScheduler.class, "runTaskTimerAsynchronously", Plugin.class, Consumer.class, long.class, long.class
		);
	}

	void cancelTasks() {
		scheduler.cancelTasks(plugin);
	}

	private static long toTicks(Duration duration) {
		return duration.toMillis() / 50L;
	}

	@Override
	public void execute(@NotNull Runnable command) {
		scheduler.runTaskAsynchronously(plugin, command);
	}

	@Override
	public ScheduledTask run(Runnable command) {
		return new PaperTask(
				scheduler.runTaskAsynchronously(plugin, command)
		);
	}

	@Override
	public void run(Consumer<ScheduledTask> command) {
		if (hasMethodRunTask) {
			scheduler.runTaskAsynchronously(plugin, (bukkitTask) -> command.accept(new PaperTask(bukkitTask)));
			return;
		}
		BukkitTaskConsumerToRunnable.setup(
				command, (runnable) -> scheduler.runTaskAsynchronously(plugin, runnable)
		);
	}

	@Override
	public ScheduledTask runDelayed(Runnable command, Duration delay) {
		return new PaperTask(
				scheduler.runTaskLaterAsynchronously(plugin, command, toTicks(delay))
		);
	}

	@Override
	public void runDelayed(Consumer<ScheduledTask> command, Duration delay) {
		long delayInTicks = toTicks(delay);
		if (hasMethodRunTaskLater) {
			scheduler.runTaskLaterAsynchronously(
					plugin, (bukkitTask) -> command.accept(new PaperTask(bukkitTask)), delayInTicks
			);
			return;
		}
		BukkitTaskConsumerToRunnable.setup(
				command, (runnable) -> scheduler.runTaskLaterAsynchronously(plugin, runnable, delayInTicks)
		);
	}

	@Override
	public ScheduledTask runAtFixedRate(Runnable command, Duration initialDelay, Duration period) {
		return new PaperTask(
				scheduler.runTaskTimerAsynchronously(plugin, command, toTicks(initialDelay), toTicks(period))
		);
	}

	@Override
	public void runAtFixedRate(Consumer<ScheduledTask> command, Duration initialDelay, Duration period) {
		long initialDelayInTicks = toTicks(initialDelay);
		long periodInTicks = toTicks(period);
		if (hasMethodRunTaskTimer) {
			scheduler.runTaskTimerAsynchronously(
					plugin, (bukkitTask) -> command.accept(new PaperTask(bukkitTask)),
					initialDelayInTicks, periodInTicks
			);
			return;
		}
		BukkitTaskConsumerToRunnable.setup(
				command,
				(runnable) -> scheduler.runTaskTimerAsynchronously(plugin, runnable, initialDelayInTicks, periodInTicks)
		);
	}

}
