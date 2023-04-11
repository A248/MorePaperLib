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

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.Arrays;

public abstract class SchedulingExtension implements ParameterResolver {

	private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SchedulingExtension.class);

	@Override
	public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		return Arrays.asList(
				RegionExecutor.class,
				AwaitableExecution.class
		).contains(parameterContext.getParameter().getType());
	}

	abstract RegionExecutor<?> createExecutor();

	private RegionExecutor<?> getExecutor(ExtensionContext extensionContext) {
		return extensionContext.getStore(NAMESPACE)
				.getOrComputeIfAbsent(RegionExecutor.class, (cls) -> createExecutor(), RegionExecutor.class);
	}

	@Override
	public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
		RegionExecutor<?> executor = getExecutor(extensionContext);
		Class<?> parameterType = parameterContext.getParameter().getType();
		if (parameterType.equals(AwaitableExecution.class)) {
			return new AwaitableExecution(executor);
		}
		return executor;
	}

}
