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

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.MorePaperLib;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(BukkitSchedulingExtension.class)
public abstract class BukkitSchedulerTestBase {

	private final Server server;
	protected final BukkitScheduler bukkitScheduler;
	private final World world;
	private final int chunkX;
	private final int chunkZ;
	private final Entity entity;

	private GracefulScheduling scheduling;
	protected RegionExecutor<?> region;
	protected AwaitableExecution command;

	public BukkitSchedulerTestBase(@Mock Server server, @Mock BukkitScheduler bukkitScheduler,
								   @Mock World world, @Mock Entity entity) {
		this.server = server;
		this.bukkitScheduler = bukkitScheduler;
		this.world = world;
		this.entity = entity;
		this.chunkX = ThreadLocalRandom.current().nextInt();
		this.chunkZ = ThreadLocalRandom.current().nextInt();
	}

	protected FoliaDetection foliaDetection() {
		return StandardFoliaDetection.INSTANCE;
	}

	private static Runnable altCallback() {
		return () -> { throw new AssertionError("Alternate callback will never run using BukkitScheduler"); };
	}

	protected static Consumer<BukkitTask> anyTaskConsumer() {
		return any();
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin, RegionExecutor<?> region, AwaitableExecution command) {
		lenient().when(plugin.getServer()).thenReturn(server);
		lenient().when(server.getScheduler()).thenReturn(bukkitScheduler);
		scheduling = new GracefulScheduling(new MorePaperLib(plugin), foliaDetection());
		this.region = region;
		this.command = command;
	}

	@Test
	public void isUsingFolia() {
		assertFalse(scheduling.isUsingFolia());
	}

	@Test
	public void runTask() {
		when(bukkitScheduler.runTaskAsynchronously(any(), (Runnable) any())).thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTask(any(), (Runnable) any())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduling.asyncScheduler().run(command));
		assertEquals(region.wrappedTask, scheduling.globalRegionalScheduler().run(command));
		assertEquals(region.wrappedTask, scheduling.regionSpecificScheduler(world, chunkX, chunkZ).run(command));
		assertEquals(region.wrappedTask, scheduling.entitySpecificScheduler(entity).run(command, altCallback()));
		verify(bukkitScheduler).runTaskAsynchronously(any(), eq(command));
		verify(bukkitScheduler, times(3)).runTask(any(), eq(command));
		assertEquals(4, command.verifyRuns());
	}

	// The Consumer-utilizing test methods have mocks and verification performed by subclasses
	@Test
	public void runTaskWithConsumer() {
		scheduling.asyncScheduler().run(command.asConsumer());
		scheduling.globalRegionalScheduler().run(command.asConsumer());
		scheduling.regionSpecificScheduler(world, chunkX, chunkZ).run(command.asConsumer());
		scheduling.entitySpecificScheduler(entity).run(command.asConsumer(), altCallback());
		assertEquals(4, command.verifyRuns());
	}

	@Test
	public void runDelayedTask() {
		when(bukkitScheduler.runTaskLaterAsynchronously(any(), (Runnable) any(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTaskLater(any(), (Runnable) any(), anyLong())).thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduling.asyncScheduler().runDelayed(command, Duration.ofMillis(100L)));
		assertEquals(region.wrappedTask, scheduling.globalRegionalScheduler().runDelayed(command, 2L));
		assertEquals(region.wrappedTask, scheduling.regionSpecificScheduler(world, chunkX, chunkZ).runDelayed(command, 2L));
		assertEquals(region.wrappedTask, scheduling.entitySpecificScheduler(entity).runDelayed(command, altCallback(), 2L));
		verify(bukkitScheduler).runTaskLaterAsynchronously(any(), eq(command), eq(2L));
		verify(bukkitScheduler, times(3)).runTaskLater(any(), eq(command), eq(2L));
		assertEquals(4, command.verifyRuns());
	}

	@Test
	public void runDelayedTaskWithConsumer() {
		scheduling.asyncScheduler().runDelayed(command.asConsumer(), Duration.ofMillis(250L));
		scheduling.globalRegionalScheduler().runDelayed(command.asConsumer(), 5L);
		scheduling.regionSpecificScheduler(world, chunkX, chunkZ).runDelayed(command.asConsumer(), 5L);
		scheduling.entitySpecificScheduler(entity).runDelayed(command.asConsumer(), altCallback(), 5L);
		assertEquals(4, command.verifyRuns());
	}

	@Test
	public void runTaskAtFixedRate() {
		when(bukkitScheduler.runTaskTimerAsynchronously(any(), (Runnable) any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		when(bukkitScheduler.runTaskTimer(any(), (Runnable) any(), anyLong(), anyLong()))
				.thenAnswer(region.runTaskWhenReceived());
		assertEquals(region.wrappedTask, scheduling.asyncScheduler().runAtFixedRate(command, Duration.ofMillis(150L), Duration.ofMillis(200L)));
		assertEquals(region.wrappedTask, scheduling.globalRegionalScheduler().runAtFixedRate(command, 3L, 4L));
		assertEquals(region.wrappedTask, scheduling.regionSpecificScheduler(world, chunkX, chunkZ).runAtFixedRate(command, 3L, 4L));
		assertEquals(region.wrappedTask, scheduling.entitySpecificScheduler(entity).runAtFixedRate(command, altCallback(), 3L, 4L));
		verify(bukkitScheduler).runTaskTimerAsynchronously(any(), eq(command), eq(3L), eq(4L));
		verify(bukkitScheduler, times(3)).runTaskTimer(any(), eq(command), eq(3L), eq(4L));
		assertEquals(4, command.verifyRuns());
	}

	@Test
	public void runTaskAtFixedRateWithConsumer() {
		scheduling.asyncScheduler().runAtFixedRate(command.asConsumer(), Duration.ofMillis(300L), Duration.ofMillis(350L));
		scheduling.globalRegionalScheduler().runAtFixedRate(command.asConsumer(), 6L, 7L);
		scheduling.regionSpecificScheduler(world, chunkX, chunkZ).runAtFixedRate(command.asConsumer(), 6L, 7L);
		scheduling.entitySpecificScheduler(entity).runAtFixedRate(command.asConsumer(), altCallback(), 6L, 7L);
		assertEquals(4, command.verifyRuns());
	}

	@Test
	public void isOnGlobalTickThread() {
		when(server.isPrimaryThread()).thenReturn(true);
		assertTrue(scheduling.isOnGlobalRegionThread());
		verify(server).isPrimaryThread();
		verifyNoMoreInteractions(server);
	}

	@AfterEach
	public void verifyNothingElse() {
		verifyNoMoreInteractions(bukkitScheduler);
	}

}
