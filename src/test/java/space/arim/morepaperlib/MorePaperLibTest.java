package space.arim.morepaperlib;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

		JavaPlugin plugin = MockPlugin.create(server, dataFolder);

		morePaperLib = new MorePaperLib(plugin);
	}

	@Test
	public void getCommandMap() {
		when(server.getCommandMap()).thenReturn(commandMap);
		assertEquals(commandMap, morePaperLib.getServerCommandMap());
	}

	@Test
	public void getCommandMapKnownCommands() {
		assertEquals(
				commandMap.getKnownCommands(),
				morePaperLib.getCommandMapKnownCommands(commandMap));
	}

	private <F> F getFieldValue(String fieldName, Class<F> fieldType) {
		return morePaperLib.getFieldValue(getClass(), this, fieldName, fieldType);
	}

	private final String stringField = "value";
	private final Object objectField = new Object();
	@Test
	public void getFieldValue() {
		assertEquals(stringField, getFieldValue("stringField", String.class), "Simple access");

		assertThrows(FeatureFailedException.class, () -> getFieldValue("objectField", String.class), "Wrong type");
		assertEquals(objectField, getFieldValue("objectField", Object.class), "Corrected type");

		assertThrows(FeatureFailedException.class, () -> getFieldValue("noField", String.class), "Does not exist");
	}

}
