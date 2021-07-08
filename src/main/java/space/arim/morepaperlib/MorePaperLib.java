/* 
 * MorePapperLib
 * Copyright © 2020 Anand Beh <https://www.arim.space>
 * 
 * MorePapperLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MorePapperLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MorePapperLib. If not, see <https://www.gnu.org/licenses/>
 * and navigate to version 3 of the GNU Lesser General Public License.
 */
package space.arim.morepaperlib;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main usage point for MorePaperLib. <br>
 * <br>
 * All public methods are implemented independently of each other.
 * 
 * @author A248
 *
 */
public class MorePaperLib {

	private final JavaPlugin plugin;
	
	/**
	 * Creates from a {@code JavaPlugin} to use
	 * 
	 * @param plugin the plugin to use
	 */
	public MorePaperLib(JavaPlugin plugin) {
		this.plugin = Objects.requireNonNull(plugin, "plugin");
	}

	/*
	Implementation details: The following methods simply try the call and
	catch NoSuchMethodError. This is fine considering they will only be used
	for command registration which is not performance-sensitive.
	 */

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
		Server server = plugin.getServer();
		try {
			return server.getCommandMap();
		} catch (NoSuchMethodError ignored) {}

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
		try {
			return simpleCommandMap.getKnownCommands();
		} catch (NoSuchMethodError ignored) {}

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
