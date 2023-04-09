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
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.arim.morepaperlib.FeatureFailedException;
import space.arim.morepaperlib.MockPlugin;
import space.arim.morepaperlib.MorePaperLib;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MorePaperLibTest {

	private final Server server;
	private SimpleCommandMap commandMap;
	private MorePaperLib morePaperLib;

	public MorePaperLibTest(@Mock Server server) {
		this.server = server;
	}

	@BeforeEach
	public void setMorePaperLib(@TempDir Path dataFolder) {
		commandMap = new SimpleCommandMap(server);

		Plugin plugin = MockPlugin.create(server, dataFolder);

		morePaperLib = new MorePaperLib(plugin);
	}

	@Test
	public void getCommandMap() {
		when(server.getCommandMap()).thenReturn(commandMap);
		assertEquals(
				commandMap,
				morePaperLib.commandRegistration().getServerCommandMap());
	}

	@Test
	public void getCommandMapKnownCommands() {
		assertEquals(
				commandMap.getKnownCommands(),
				morePaperLib.commandRegistration().getCommandMapKnownCommands(commandMap)
		);
	}

	private <F> F getFieldValue(String fieldName, Class<F> fieldType) {
		return morePaperLib.commandRegistration().getFieldValue(getClass(), this, fieldName, fieldType);
	}

	private final String stringField = "value";
	private final Object objectField = new Object();
	@Test
	public void getFieldValue() {
		assertEquals(stringField, getFieldValue("stringField", String.class), "Simple access");

		Assertions.assertThrows(FeatureFailedException.class, () -> getFieldValue("objectField", String.class), "Wrong type");
		assertEquals(objectField, getFieldValue("objectField", Object.class), "Corrected type");

		assertThrows(FeatureFailedException.class, () -> getFieldValue("noField", String.class), "Does not exist");
	}

}
