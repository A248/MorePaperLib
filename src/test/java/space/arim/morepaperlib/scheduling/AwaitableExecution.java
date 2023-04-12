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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class AwaitableExecution implements Runnable {

	private final RegionExecutor<?> regionExecutor;
	private final AtomicInteger runCounter = new AtomicInteger();

	AwaitableExecution(RegionExecutor<?> regionExecutor) {
		this.regionExecutor = regionExecutor;
	}

	@Override
	public void run() {
		runCounter.getAndIncrement();
	}

	public <T> Consumer<T> asConsumer() {
		return (task) -> run();
	}

	public boolean verifyIfRan() {
		return verifyRuns() != 0;
	}

	public int verifyRuns() {
		regionExecutor.awaitTasks();
		return runCounter.getAndIncrement();
	}

}
