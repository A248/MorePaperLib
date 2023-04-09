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

package space.arim.morepaperlib;

import org.bukkit.plugin.Plugin;
import space.arim.morepaperlib.commands.CommandRegistration;
import space.arim.morepaperlib.scheduling.GracefulScheduling;

import java.util.Objects;

/**
 * Main usage point for MorePaperLib
 *
 */
public class MorePaperLib {

	private final Plugin plugin;

	/**
	 * Creates from a {@code Plugin} to use
	 * 
	 * @param plugin the plugin to use
	 */
	public MorePaperLib(Plugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
	}

	/**
	 * Returns the plugin associated with this
	 *
	 * @return the plugin
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * This method is used by MorePaperLib to detect the presence of certain features. It may
	 * be used by callers as a convenience. It may also be overridden to force the usage of specific
	 * API, but please note that the calling patterns used by MorePaperLib are strictly unspecified.
	 *
	 * @param clazz the class in which to check
	 * @param methodName the name of the method
	 * @param parameterTypes the raw parameter types
	 * @return true if it exists
	 */
	public boolean methodExists(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
		try {
			clazz.getMethod(methodName, parameterTypes);
			return true;
		} catch (NoSuchMethodException ex) {
			return false;
		}
	}

	/**
	 * Accesses command registration
	 *
	 * @return command registration
	 */
	public CommandRegistration commandRegistration() {
		return new CommandRegistration(this);
	}

	/**
	 * Accesses scheduling
	 *
	 * @return the scheduling wrapper
	 */
	public GracefulScheduling scheduling() {
		return new GracefulScheduling(this);
	}

}
