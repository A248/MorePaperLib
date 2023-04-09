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

package space.arim.morepaperlib.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import space.arim.morepaperlib.FeatureFailedException;
import space.arim.morepaperlib.MorePaperLib;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Methods relating to runtime command registration
 *
 */
public final class CommandRegistration {

	private final MorePaperLib morePaperLib;

	private final boolean hasMethodGetCommandMap;
	private final boolean hasMethodGetKnownCommands;

	public CommandRegistration(MorePaperLib morePaperLib) {
		this.morePaperLib = morePaperLib;
		hasMethodGetCommandMap = morePaperLib.methodExists(Server.class, "getCommandMap");
		hasMethodGetKnownCommands = morePaperLib.methodExists(SimpleCommandMap.class, "getKnownCommands");
	}

	/**
	 * Gets the server's command map. <br>
	 * <br>
	 * If the method {@code Server#getCommandMap()} exists, it will be used. Otherwise,
	 * reflectively acceses the field 'commandMap'.
	 *
	 * @return the server command map
	 * @throws FeatureFailedException if all attempts to get the command map failed
	 */
	public CommandMap getServerCommandMap() {
		Server server = morePaperLib.getPlugin().getServer();
		if (hasMethodGetCommandMap) {
			return server.getCommandMap();
		}
		return getFieldValue(server.getClass(), server, "commandMap", CommandMap.class);
	}

	/**
	 * Gets known commands map. <br>
	 * <br>
	 * Uses {@code SimpleCommandMap#getKnownCommands()} if the method exists. Otherwise
	 * reflectively accesses the field 'knownCommands'.
	 *
	 * @param simpleCommandMap the simple command map
	 * @return the map of known commands
	 * @throws FeatureFailedException if the known commands could not be retrieved
	 */
	public Map<String, Command> getCommandMapKnownCommands(SimpleCommandMap simpleCommandMap) {
		if (hasMethodGetKnownCommands) {
			return simpleCommandMap.getKnownCommands();
		}
		@SuppressWarnings("unchecked")
		Map<String, Command> knownCommands = (Map<String, Command>)
				getFieldValue(SimpleCommandMap.class, simpleCommandMap, "knownCommands", Map.class);
		return knownCommands;
	}

	// Visible for testing
	<T, F> F getFieldValue(Class<? extends T> clazz, T object, String fieldName, Class<F> fieldType) {
		Field field;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException | SecurityException ex) {
			throw new FeatureFailedException("Unable to find field " + fieldName, ex);
		}
		Object fieldValue;
		try {
			field.setAccessible(true);
			fieldValue = field.get(object);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
			throw new FeatureFailedException("Unable to access field " + fieldName, ex);
		}
		if (!fieldType.isInstance(fieldValue)) {
			throw new FeatureFailedException(
					"Field " + fieldName + " not an instance of " + fieldType.getName());
		}
		return fieldType.cast(fieldValue);
	}

}
