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

import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(FoliaSchedulingExtension.class)
public class RegionSchedulerTest {

	private final RegionScheduler regionScheduler;
	private final World world;
	private final int chunkX;
	private final int chunkZ;
	private RegionalScheduler scheduler;

	private RegionExecutor<?> region;
	private AwaitableExecution command;

	public RegionSchedulerTest(@Mock RegionScheduler regionScheduler,
							   @Mock World world) {
		this.regionScheduler = regionScheduler;
		this.world = world;
		this.chunkX = ThreadLocalRandom.current().nextInt();
		this.chunkZ = ThreadLocalRandom.current().nextInt();
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin, @Mock Server server,
					  RegionExecutor<?> region, AwaitableExecution command) {
		when(plugin.getServer()).thenReturn(server);
		when(server.getRegionScheduler()).thenReturn(regionScheduler);
		scheduler = new GracefulScheduling(new MorePaperLib(plugin), FoliaDetection.enabled())
				.regionSpecificScheduler(world, chunkX, chunkZ);
		this.region = region;
		this.command = command;
		region.setArgumentOffset(3);
	}

	@Test
	public void runTask() {
		when(regionScheduler.run(any(), any(), anyInt(), anyInt(), any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.run(command));
		verify(regionScheduler).run(any(), eq(world), eq(chunkX), eq(chunkZ), any());
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskDelayed() {
		when(regionScheduler.runDelayed(any(), any(), anyInt(), anyInt(), any(), anyLong())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runDelayed(command, 6L));
		verify(regionScheduler).runDelayed(any(), eq(world), eq(chunkX), eq(chunkZ), any(), eq(6L));
		assertTrue(command.verifyIfRan());
	}

	@Test
	public void runTaskAtFixedRate() {
		when(regionScheduler.runAtFixedRate(any(), any(), anyInt(), anyInt(), any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runAtFixedRate(command, 3L, 2L));
		verify(regionScheduler).runAtFixedRate(any(), eq(world), eq(chunkX), eq(chunkZ), any(), eq(3L), eq(2L));
		assertTrue(command.verifyIfRan());
	}

	@AfterEach
	public void verifyNothingElse() {
		verifyNoMoreInteractions(regionScheduler);
	}

}
