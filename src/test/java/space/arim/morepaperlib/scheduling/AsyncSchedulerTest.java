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
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(FoliaSchedulingExtension.class)
public class AsyncSchedulerTest {

	private final AsyncScheduler asyncScheduler;
	private AsynchronousScheduler scheduler;

	private RegionExecutor<?> region;
	private AwaitableExecution command;

	public AsyncSchedulerTest(@Mock AsyncScheduler asyncScheduler) {
		this.asyncScheduler = asyncScheduler;
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin, @Mock Server server, 
					  RegionExecutor<?> region, AwaitableExecution command) {
		when(plugin.getServer()).thenReturn(server);
		when(server.getAsyncScheduler()).thenReturn(asyncScheduler);
		scheduler = new GracefulScheduling(new MorePaperLib(plugin), FoliaDetection.enabled()).asyncScheduler();
		this.region = region;
		this.command = command;
	}

	@Test
	public void runTask() {
		when(asyncScheduler.runNow(any(), any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.run(command));
		verify(asyncScheduler).runNow(any(), any());
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskDelayed() {
		long delay = ThreadLocalRandom.current().nextLong();
		when(asyncScheduler.runDelayed(any(), any(), anyLong(), any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runDelayed(command, Duration.ofNanos(delay)));
		verify(asyncScheduler).runDelayed(any(), any(), eq(delay), eq(TimeUnit.NANOSECONDS));
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskAtFixedRate() {
		long initialDelay = ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE);
		long period = ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE);
		when(asyncScheduler.runAtFixedRate(any(), any(), anyLong(), anyLong(), any()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(
				region.wrappedTask,
				scheduler.runAtFixedRate(command, Duration.ofNanos(initialDelay), Duration.ofNanos(period))
		);
		verify(asyncScheduler).runAtFixedRate(any(), any(), eq(initialDelay), eq(period), eq(TimeUnit.NANOSECONDS));
		assertTrue(command.verifyIfRan());
	}

	@AfterEach
	public void verifyNothingElse() {
		verifyNoMoreInteractions(asyncScheduler);
	}

}
