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

import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

public final class BukkitSchedulingExtension extends SchedulingExtension {

	@Override
	protected RegionExecutor<?> createExecutor() {
		final class BukkitRegionExecutor extends RegionExecutor<BukkitTask> {

			private BukkitRegionExecutor(ExecutorService executor, BukkitTask platformTask, ScheduledTask wrappedTask) {
				super(executor, platformTask, wrappedTask);
			}

		}
		BukkitTask task = mock(BukkitTask.class);
		return new BukkitRegionExecutor(
				Executors.newSingleThreadExecutor(), task, new PaperTask(task)
		);
	}

}
