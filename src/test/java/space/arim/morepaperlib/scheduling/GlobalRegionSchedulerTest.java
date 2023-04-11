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
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

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
public class GlobalRegionSchedulerTest {

	private final GlobalRegionScheduler globalRegionScheduler;
	private RegionalScheduler scheduler;

	private RegionExecutor<?> region;
	private AwaitableExecution command;

	public GlobalRegionSchedulerTest(@Mock GlobalRegionScheduler globalRegionScheduler) {
		this.globalRegionScheduler = globalRegionScheduler;
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin, @Mock Server server, 
					  RegionExecutor<?> region, AwaitableExecution command) {
		when(plugin.getServer()).thenReturn(server);
		when(server.getGlobalRegionScheduler()).thenReturn(globalRegionScheduler);
		scheduler = new MorePaperLib(plugin).scheduling().globalRegionalScheduler();
		this.region = region;
		this.command = command;
	}

	@Test
	public void runTask() {
		when(globalRegionScheduler.run(any(), any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.run(command));
		verify(globalRegionScheduler).run(any(), any());
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskDelayed() {
		when(globalRegionScheduler.runDelayed(any(), any(), anyLong())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runDelayed(command, 6L));
		verify(globalRegionScheduler).runDelayed(any(), any(), eq(6L));
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskAtFixedRate() {
		when(globalRegionScheduler.runAtFixedRate(any(), any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runAtFixedRate(command, 3L, 2L));
		verify(globalRegionScheduler).runAtFixedRate(any(), any(), eq(3L), eq(2L));
		assertTrue(command.verifyIfRan());
	}

	@AfterEach
	public void verifyNothingElse() {
		verifyNoMoreInteractions(globalRegionScheduler);
	}

}
