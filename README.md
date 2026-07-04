
# MorePaperLib

[Discord]:https://img.shields.io/discord/784154359067443280
[License]:https://www.gnu.org/graphics/lgplv3-147x51.png
[GitHubStar]:https://img.shields.io/github/stars/A248/MorePaperLib

[![Discord]](https://discord.gg/3C4qeG8XhE) ![GitHubStar]

![License]

### Gracefully and simultaneously support Spigot, Paper, Folia, etc.

Need to support Folia? Spigot? Multiple versions of Paper and/or adventure?

Well, MorePaperLib has you covered. Now you can effortlessly support multiple versions of these platforms. MorePaperLib covers common use cases in an efficient manner, depending on the APIs that are available, without reflection.

### Testing and Environments

Simple, useful libraries should have one purpose and carry it out effectively, without bugs.

* To achieve this goal, we implement integration testing for all supported features, on 8 different platforms:
  * Spigot 1.8.8
  * Spigot 1.16.4
  * Paper 1.16.5 (Adventure introduced)
  * Paper 1.19.4 (pre-Folia)
  * Paper 1.20.1 (post-Folia)
  * Paper 26.2 (build 19 in Maven, last version with Adventure 4.26)
  * Paper 26.2 (build 47, Adventure 5.1)
  * Folia 1.19.4
* Additionally, the library is manually tested through its downstream plugins, such as in [LibertyBans](https://github.com/A248/LibertyBans).

Java 8 compatibility is guaranteed.

## Features

This library is basically a grab bag of miscellaneous features.

Here are some examples.

```java
Plugin plugin = /* your plugin */;
MorePaperLib morePaperLib = new MorePaperLib(plugin);

// Scheduling (Folia compatible)
morePaperLib.scheduling().asyncScheduler().run(() -> plugin.getLogger().info("This runs asynchronously"));
morePaperLib.scheduling().regionScheduler(new Location(10, 64, 15, Bukkit.getWorld("world_nether"))).run(() -> plugin.getLogger().info("This runs on the region at 10, 64, 15 in 'world_nether'"));
morePaperLib.cancelGlobalTasks();

// Command map
CommandMap commandMap = morePaperLib.commandRegistration().getServerCommandMap();

// Adventure: Component#toBuilder / BuildableComponent
MorePaperLibAdventure morePaperLibAdventure = new MorePaperLibAdventure(morePaperLib);
Component component = /* your component */;
@Nullable ComponentBuilder builder = morePaperLibAdventure.toBuilder(component);
```

### Folia scheduling

Paper's Folia project refactored the scheduling APIs. MorePaperLib provides a scheduling API that automatically switches between the Bukkit scheduler and the Folia schedulers.

Supported aspects:
* Regional scheduling, global scheduling, `isGlobalTickThread()`, etc.
* Canceling tasks where possible (Folia region-specific tasks cannot be canceled)
* Graceful compatibility with Spigot and older Paper versions

Correct usage of the Folia API is critical to server success. Unlike other Folia wrappers you may have found, the correctness of this library is guaranteed by the reputation of this author. I have personally already noticed, and reported, bugs in other Folia wrappers based on API abuse.

### Access the command map

On Spigot, accessing the command map must be done with reflection. On Paper there is supported API: `server.getCommandMap()`. Relying on API, if it exists, makes your plugin more stable and impervious to future changes in the Paper implementation.

MorePaperLib's `getServerCommandMap` method automatically uses whichever approach is suitable to the platform.

### Adventure methods

Adventure support does **not** put Adventure on your plugin's classpath. Instead, it assumes that adventure APIs are already available. For example, this works well with dynamic dependency loading where your plugin downloads Adventure as a library for Spigot platforms, but it uses the built-in Adventure on Paper platforms.

**Adventure 4/5 compatibility**

Paper requires Adventure 5.1 since Minecraft 26.2, However, older Paper server versions use older Adventure versions, going back to Adventure 4.7 in Paper 1.16.5. We provide a compatibility layer that works with all Adventure versions, 4.7-4.26 and 5.1+.

For each feature, the new method is used if available. Otherwise, the library falls back to the old method.
* Components into builders
  * Uses `Component#toBuilder` if available, compiling against the new method (future-proof for Adventure 6)
  * `BuildableComponent` when necessary.
* Components of children: `Component.textOfChildren` / `TextComponent.ofChildren`
* Click event actions (ClickEvent.Action)
* Plain text component serializer.
  * They deprecated PlainComponentSerializer in 4.8.0 and renamed it to PlainTextComponentSerializer. Then in 5.0, the old interface was removed.
  * This feature lets you use either class depending on which is available.
* Detect Adventure version

**Server operations**

Kick players with a `Component`. Disallow login events with a `Component` reason. If Adventure is not supported, fallback occurs automatically to legacy text serialization.

## Library Details

* Java 8
* Supports Spigot and Paper from 1.8.8 to the latest version.
* Supports any version of Adventure from 4.7-4.26 or 5.1+, when Adventure is on the classpath.
* Compatibility with Folia is guaranteed only for the latest Folia release, because Folia is unstable software.

### Artifact Info

The artifact is:

```xml
<dependency>
    <groupId>space.arim.morepaperlib</groupId>
    <artifactId>morepaperlib</artifactId>
    <version>0.4.4</version>
</dependency>
```

From repository:

```xml
<repository>
    <id>arim-mvn-lgpl3</id>
    <url>https://mvn-repo.arim.space/lesser-gpl3/</url>
</repository>
```

### License

Licensed under the GNU Lesser GPL v3, or later. See the license file for details.
