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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import space.arim.morepaperlib.MorePaperLib;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntitySpecificSchedulerTest {

	private final Entity entity;
	private final EntityScheduler entityScheduler;
	private final io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask;
	private final space.arim.morepaperlib.scheduling.ScheduledTask wrappedTask;
	private AttachedScheduler scheduler;

	private CompletableFuture<Void> ranCommand;
	private CompletableFuture<Void> ranAlternate;
	private final Runnable command = () -> ranCommand.complete(null);
	private final Runnable alternate = () -> ranAlternate.complete(null);

	private final Executor executorThread = Executors.newSingleThreadExecutor();

	public EntitySpecificSchedulerTest(@Mock Entity entity, @Mock EntityScheduler entityScheduler,
									   @Mock io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask) {
		this.entity = entity;
		this.entityScheduler = entityScheduler;
		this.foliaTask = foliaTask;
		wrappedTask = new FoliaTask(foliaTask);
	}

	@BeforeEach
	public void setup(@Mock Plugin plugin) {
		when(entity.getScheduler()).thenReturn(entityScheduler);
		scheduler = new MorePaperLib(plugin).scheduling().entitySpecificScheduler(entity);
		ranCommand = new CompletableFuture<>();
		ranAlternate = new CompletableFuture<>();
	}

	private Answer<Object> runTaskWhenReceived() {
		return invocationOnMock -> {
			Consumer<io.papermc.paper.threadedregions.scheduler.ScheduledTask> taskConsumer = invocationOnMock.getArgument(1);
			ForkJoinPool.commonPool().execute(() -> taskConsumer.accept(foliaTask));
			return foliaTask;
		};
	}

	private Answer<Object> runAlternateWhenReceived() {
		return invocationOnMock -> {
			Runnable alternate = invocationOnMock.getArgument(2);
			alternate.run();
			return foliaTask;
		};
	}

	private void assertCompleteAndNotComplete(CompletableFuture<?> complete, CompletableFuture<?> notComplete) {
		// Rely on FIFO property of the executor
		CompletableFuture.runAsync(() -> {}, executorThread).join();
		assertTrue(complete.isDone());
		assertFalse(notComplete.isDone());
	}

	private void assertNeitherComplete() {
		CompletableFuture.runAsync(() -> {}, executorThread).join();
		assertFalse(ranCommand.isDone());
		assertFalse(ranAlternate.isDone());
	}

	@Test
	public void runTask() {
		when(entityScheduler.run(any(), any(), any())).thenAnswer(runTaskWhenReceived());
		assertEquals(wrappedTask, scheduler.run(command, alternate));
		verify(entityScheduler).run(any(), any(), eq(alternate));
		assertCompleteAndNotComplete(ranCommand, ranAlternate);
	}

	@Test
	public void runRejectedTask() {
		assertNull(scheduler.run(command, alternate));
		assertNeitherComplete();
	}

	@Test
	public void runAlternateTask() {
		when(entityScheduler.run(any(), any(), any())).thenAnswer(runAlternateWhenReceived());
		assertEquals(wrappedTask, scheduler.run(command, alternate));
		verify(entityScheduler).run(any(), any(), eq(alternate));
		assertCompleteAndNotComplete(ranAlternate, ranCommand);
	}

	@Test
	public void runTaskDelayed() {
		when(entityScheduler.runDelayed(any(), any(), any(), anyLong())).thenAnswer(runTaskWhenReceived());
		assertEquals(wrappedTask, scheduler.runDelayed(command, alternate, 6L));
		verify(entityScheduler).runDelayed(any(), any(), eq(alternate), eq(6L));
		assertCompleteAndNotComplete(ranCommand, ranAlternate);
	}

	@Test
	public void runRejectedTaskDelayed() {
		assertNull(scheduler.runDelayed(command, alternate, 4L));
		assertNeitherComplete();
	}

	@Test
	public void runAlternateTaskDelayed() {
		when(entityScheduler.runDelayed(any(), any(), any(), anyLong())).thenAnswer(runAlternateWhenReceived());
		assertEquals(wrappedTask, scheduler.runDelayed(command, alternate, 7L));
		verify(entityScheduler).runDelayed(any(), any(), eq(alternate), eq(7L));
		assertCompleteAndNotComplete(ranAlternate, ranCommand);
	}

	@Test
	public void runTaskAtFixedRate() {
		when(entityScheduler.runAtFixedRate(any(), any(), any(), anyLong(), anyLong())).thenAnswer(runTaskWhenReceived());
		assertEquals(wrappedTask, scheduler.runAtFixedRate(command, alternate, 3L, 2L));
		verify(entityScheduler).runAtFixedRate(any(), any(), eq(alternate), eq(3L), eq(2L));
		assertCompleteAndNotComplete(ranCommand, ranAlternate);
	}

	@Test
	public void runRejectedTaskAtFixedRate() {
		assertNull(scheduler.runAtFixedRate(command, alternate, 2L, 3L));
		assertNeitherComplete();
	}

	@Test
	public void runAlternateTaskAtFixedRate() {
		when(entityScheduler.runAtFixedRate(any(), any(), any(), anyLong(), anyLong())).thenAnswer(runAlternateWhenReceived());
		assertEquals(wrappedTask, scheduler.runAtFixedRate(command, alternate, 8L, 9L));
		verify(entityScheduler).runAtFixedRate(any(), any(), eq(alternate), eq(8L), eq(9L));
		assertCompleteAndNotComplete(ranAlternate, ranCommand);
	}

}
