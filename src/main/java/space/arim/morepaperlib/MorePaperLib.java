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
import java.util.Objects;

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main usage point for MorePaperLib
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
	
	/**
	 * Gets the server's command map. <br>
	 * <br>
	 * If the method {@code Server#getCommandMap()} exists, it will be used. Otherwise,
	 * reflection will be attempted.
	 * 
	 * @return the server command map
	 * @throws FeatureFailedException if all attempts to get the command map failed
	 */
	public CommandMap getServerCommandMap() {
		Server server = plugin.getServer();
		try {
			return server.getCommandMap();
		} catch (NoSuchMethodError ignored) {}

		Field commandMapField;
		try {
			commandMapField = server.getClass().getDeclaredField("commandMap");
		} catch (NoSuchFieldException | SecurityException ex) {
			throw new FeatureFailedException("Unable to find commandMap field", ex);
		}
		try {
			commandMapField.setAccessible(true);
			return (CommandMap) commandMapField.get(server);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException ex) {
			throw new FeatureFailedException(ex);
		}
	}
	
}
