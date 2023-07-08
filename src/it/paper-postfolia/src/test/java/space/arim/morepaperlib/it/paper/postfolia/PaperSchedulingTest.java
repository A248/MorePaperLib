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

package space.arim.morepaperlib.it.paper.postfolia;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import space.arim.morepaperlib.scheduling.BukkitSchedulerTestBase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PaperSchedulingTest extends BukkitSchedulerTestBase {

	public PaperSchedulingTest(@Mock Server server, @Mock BukkitScheduler bukkitScheduler,
							   @Mock World world, @Mock Entity entity) {
		super(server, bukkitScheduler, world, entity);
	}

	@Test
	@Override
	public void runTaskWithConsumer() {
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler).runTaskAsynchronously(any(), anyTaskConsumer());
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler).runTask(any(), anyTaskConsumer());
		super.runTaskWithConsumer();
		verify(bukkitScheduler).runTaskAsynchronously(any(), anyTaskConsumer());
		verify(bukkitScheduler, times(3)).runTask(any(), anyTaskConsumer());
	}

	@Test
	@Override
	public void runDelayedTaskWithConsumer() {
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler).runTaskLaterAsynchronously(any(), anyTaskConsumer(), anyLong());
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler).runTaskLater(any(), anyTaskConsumer(), anyLong());
		super.runDelayedTaskWithConsumer();
		verify(bukkitScheduler).runTaskLaterAsynchronously(any(), anyTaskConsumer(), anyLong());
		verify(bukkitScheduler, times(3)).runTaskLater(any(), anyTaskConsumer(), anyLong());
	}

	@Test
	@Override
	public void runTaskAtFixedRateWithConsumer() {
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler)
				.runTaskTimerAsynchronously(any(), anyTaskConsumer(), anyLong(), anyLong());
		doAnswer(region.runTaskWhenReceived()).when(bukkitScheduler)
				.runTaskTimer(any(), anyTaskConsumer(), anyLong(), anyLong());
		super.runTaskAtFixedRateWithConsumer();
		verify(bukkitScheduler).runTaskTimerAsynchronously(any(), anyTaskConsumer(), anyLong(), anyLong());
		verify(bukkitScheduler, times(3)).runTaskTimer(any(), anyTaskConsumer(), anyLong(), anyLong());
	}

}
