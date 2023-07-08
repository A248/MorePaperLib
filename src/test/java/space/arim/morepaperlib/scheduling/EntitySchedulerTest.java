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

import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(FoliaSchedulingExtension.class)
public class EntitySchedulerTest {

	private final Entity entity;
	private final EntityScheduler entityScheduler;
	private AttachedScheduler scheduler;

	private RegionExecutor<?> region;
	private AwaitableExecution command;
	private AwaitableExecution alternate;

	public EntitySchedulerTest(@Mock Entity entity, @Mock EntityScheduler entityScheduler) {
		this.entity = entity;
		this.entityScheduler = entityScheduler;
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin, 
					  RegionExecutor<?> region, AwaitableExecution command, AwaitableExecution alternate) {
		when(entity.getScheduler()).thenReturn(entityScheduler);
		scheduler = new GracefulScheduling(new MorePaperLib(plugin), FoliaDetection.enabled()).entitySpecificScheduler(entity);
		this.region = region;
		this.command = command;
		this.alternate = alternate;
	}

	@Test
	public void runTask() {
		when(entityScheduler.run(any(), any(), any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.run(command, alternate));
		verify(entityScheduler).run(any(), any(), eq(alternate));
		assertTrue(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runRejectedTask() {
		assertNull(scheduler.run(command, alternate));
		verify(entityScheduler).run(any(), any(), eq(alternate));
		assertFalse(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runAlternateTask() {
		when(entityScheduler.run(any(), any(), any())).thenAnswer(region.runAlternateWhenReceived());
		assertEquals(region.wrappedTask, scheduler.run(command, alternate));
		verify(entityScheduler).run(any(), any(), eq(alternate));
		assertFalse(command.verifyIfRan());
		assertTrue(alternate.verifyIfRan());
	}

	@Test
	public void runTaskDelayed() {
		when(entityScheduler.runDelayed(any(), any(), any(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runDelayed(command, alternate, 6L));
		verify(entityScheduler).runDelayed(any(), any(), eq(alternate), eq(6L));
		assertTrue(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runRejectedTaskDelayed() {
		assertNull(scheduler.runDelayed(command, alternate, 4L));
		verify(entityScheduler).runDelayed(any(), any(), eq(alternate), eq(4L));
		assertFalse(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runAlternateTaskDelayed() {
		when(entityScheduler.runDelayed(any(), any(), any(), anyLong()))
				.thenAnswer(region.runAlternateWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runDelayed(command, alternate, 7L));
		verify(entityScheduler).runDelayed(any(), any(), eq(alternate), eq(7L));
		assertFalse(command.verifyIfRan());
		assertTrue(alternate.verifyIfRan());
	}

	@Test
	public void runTaskAtFixedRate() {
		when(entityScheduler.runAtFixedRate(any(), any(), any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runAtFixedRate(command, alternate, 3L, 2L));
		verify(entityScheduler).runAtFixedRate(any(), any(), eq(alternate), eq(3L), eq(2L));
		assertTrue(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runRejectedTaskAtFixedRate() {
		assertNull(scheduler.runAtFixedRate(command, alternate, 2L, 3L));
		verify(entityScheduler).runAtFixedRate(any(), any(), eq(alternate), eq(2L), eq(3L));
		assertFalse(command.verifyIfRan());
		assertFalse(alternate.verifyIfRan());
	}

	@Test
	public void runAlternateTaskAtFixedRate() {
		when(entityScheduler.runAtFixedRate(any(), any(), any(), anyLong(), anyLong()))
				.thenAnswer(region.runAlternateWhenReceived());
		assertEquals(region.wrappedTask, scheduler.runAtFixedRate(command, alternate, 8L, 9L));
		verify(entityScheduler).runAtFixedRate(any(), any(), eq(alternate), eq(8L), eq(9L));
		assertFalse(command.verifyIfRan());
		assertTrue(alternate.verifyIfRan());
	}

	@AfterEach
	public void verifyNothingElse() {
		verifyNoMoreInteractions(entityScheduler);
	}

}
