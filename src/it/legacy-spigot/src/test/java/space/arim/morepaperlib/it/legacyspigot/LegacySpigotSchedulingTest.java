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

package space.arim.morepaperlib.it.legacyspigot;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import space.arim.morepaperlib.scheduling.BukkitSchedulerTestBase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LegacySpigotSchedulingTest extends BukkitSchedulerTestBase {

	public LegacySpigotSchedulingTest(@Mock BukkitScheduler bukkitScheduler, @Mock World world, @Mock Entity entity) {
		super(bukkitScheduler, world, entity);
	}

	@Test
	@Override
	public void runTaskWithConsumer() {
		when(bukkitScheduler.runTaskAsynchronously(any(), (Runnable) any())).thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTask(any(), (Runnable) any())).thenAnswer(region.runTaskWhenReceived());
		super.runTaskWithConsumer();
		verify(bukkitScheduler).runTaskAsynchronously(any(), (Runnable) any());
		verify(bukkitScheduler, times(3)).runTask(any(), (Runnable) any());
	}

	@Test
	@Override
	public void runDelayedTaskWithConsumer() {
		when(bukkitScheduler.runTaskLaterAsynchronously(any(), (Runnable) any(), anyLong())).thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTaskLater(any(), (Runnable) any(), anyLong())).thenAnswer(region.runTaskWhenReceived());
		super.runDelayedTaskWithConsumer();
		verify(bukkitScheduler).runTaskLaterAsynchronously(any(), (Runnable) any(), anyLong());
		verify(bukkitScheduler, times(3)).runTaskLater(any(), (Runnable) any(), anyLong());
	}

	@Test
	@Override
	public void runTaskAtFixedRateWithConsumer() {
		when(bukkitScheduler.runTaskTimerAsynchronously(any(), (Runnable) any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTaskTimer(any(), (Runnable) any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		super.runTaskAtFixedRateWithConsumer();
		verify(bukkitScheduler).runTaskTimerAsynchronously(any(), (Runnable) any(), anyLong(), anyLong());
		verify(bukkitScheduler, times(3)).runTaskTimer(any(), (Runnable) any(), anyLong(), anyLong());
	}

}
