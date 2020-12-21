package space.arim.morepaperlib;

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MorePaperLibTest {

	@TempDir
	public File dataFolder;

	private MorePaperLib morePaperLib;

	@BeforeEach
	public void setup() {
		Server server = mock(Server.class);
		CommandMap commandMap = new SimpleCommandMap(server);
		when(server.getCommandMap()).thenReturn(commandMap);

		PluginDescriptionFile descriptionFile = new PluginDescriptionFile("morepaperlibtest", "none", "none");
		JavaPlugin plugin = new JavaPlugin(
				new JavaPluginLoader(server), descriptionFile, dataFolder, dataFolder) {};

		morePaperLib = new MorePaperLib(plugin);
	}

	@Test
	public void getCommandMap() {
		assertNotNull(morePaperLib.getServerCommandMap());
	}

	@Test
	public void getKnownCommands() {
		CommandMap commandMap = morePaperLib.getServerCommandMap();
		assumeTrue(commandMap instanceof SimpleCommandMap);

		assertEquals(
				morePaperLib.getCommandMapKnownCommands((SimpleCommandMap) commandMap),
				commandMap.getKnownCommands());
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
