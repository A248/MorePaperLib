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

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.stubbing.Answer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public abstract class RegionExecutor<T> implements Executor, ExtensionContext.Store.CloseableResource {

	private final ExecutorService executor;
	private final T platformTask;
	public final ScheduledTask wrappedTask;

	private volatile int argumentOffset;

	public RegionExecutor(ExecutorService executor, T platformTask, ScheduledTask wrappedTask) {
		this.executor = executor;
		this.platformTask = platformTask;
		this.wrappedTask = wrappedTask;
	}

	void setArgumentOffset(int argumentOffset) {
		this.argumentOffset = argumentOffset;
	}

	@Override
	public void execute(@NotNull Runnable runnable) {
		executor.execute(runnable);
	}

	public Answer<Object> runTaskWhenReceived() {
		return invocationOnMock -> {
			Object taskConsumer = invocationOnMock.getArgument(argumentOffset + 1);
			if (taskConsumer instanceof Runnable) {
				executor.execute((Runnable) taskConsumer);
			} else {
				@SuppressWarnings("unchecked")
				Consumer<T> casted = (Consumer<T>) taskConsumer;
				executor.execute(() -> casted.accept(platformTask));
			}
			return platformTask;
		};
	}

	Answer<Object> runAlternateWhenReceived() {
		return invocationOnMock -> {
			Runnable alternate = invocationOnMock.getArgument(argumentOffset + 2);
			alternate.run();
			return platformTask;
		};
	}

	void awaitTasks() {
		// Rely on FIFO property of the executor
		CompletableFuture.runAsync(() -> {}, executor).join();
	}

	@Override
	public void close() throws Throwable {
		executor.shutdown();
		executor.awaitTermination(1L, TimeUnit.SECONDS);
	}

}
