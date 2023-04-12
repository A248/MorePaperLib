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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

public final class FoliaSchedulingExtension extends SchedulingExtension {

	@Override
	protected RegionExecutor<?> createExecutor() {
		final class FoliaRegionExecutor
				extends RegionExecutor<io.papermc.paper.threadedregions.scheduler.ScheduledTask> {

			private FoliaRegionExecutor(ExecutorService executor,
										io.papermc.paper.threadedregions.scheduler.ScheduledTask platformTask,
										ScheduledTask wrappedTask) {
				super(executor, platformTask, wrappedTask);
			}

		}
		io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask =
				mock(io.papermc.paper.threadedregions.scheduler.ScheduledTask.class);
		return new FoliaRegionExecutor(
				Executors.newSingleThreadExecutor(), foliaTask, new FoliaTask(foliaTask)
		);
	}

}
