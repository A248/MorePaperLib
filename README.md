
# MorePaperLib

[Discord]:https://img.shields.io/discord/784154359067443280
[License]:https://www.gnu.org/graphics/lgplv3-147x51.png
[GitHubStar]:https://img.shields.io/github/stars/A248/MorePaperLib

[![Discord]](https://discord.gg/3C4qeG8XhE) ![GitHubStar]

![License]

### Gracefully and simultaneously support Spigot, Paper, Folia, etc.

Need to support Folia? Spigot? Well, MorePaperLib has you covered. Now you can effortlessly support multiple versions of Spigot, Paper, **and** Folia.

MorePaperLib covers common use cases which may require different approaches depending on the target platform. It also provides shortcuts and optimizations for platforms supporting certain API.

### Testing Rigor

*This library handles a dynamic environment with compatibility for multiple APIs, so you don't have to. However, this means it is more susceptible to platform-dependent bugs than typical libraries. We therefore recommend you stay up-to-date with releases (which are infrequent).*

Simple, useful libraries should have one purpose and carry it out effectively. There should be no bugs in such an elemental use-case.
* MorePaperLib is covered by rigorous unit test and integration tests for every feature on every supported platform.
* Additionally, the library is manually tested through its downstream plugins, such as in  [LibertyBans](https://github.com/A248/LibertyBans).

### Examples

```java
Plugin plugin = /* your plugin */;
MorePaperLib morePaperLib = new MorePaperLib(plugin);

// Scheduling
morePaperLib.scheduling().asyncScheduler().run(() -> plugin.getLogger().info("This runs asynchronously"));
morePaperLib.scheduling().regionScheduler(new Location(10, 64, 15, Bukkit.getWorld("world_nether"))).run(() -> plugin.getLogger().info("This runs on the region at 10, 64, 15 in 'world_nether'"));
morePaperLib.cancelGlobalTasks();

// Command map
CommandMap commandMap = morePaperLib.commandRegistration().getServerCommandMap();
```

## Features

### Folia scheduling

Paper's Folia project refactored the scheduling APIs. MorePaperLib provides a scheduling API that automatically switches between the Bukkit scheduler and the Folia schedulers.

Correct usage of the Folia API is critical to server success. Unlike other Folia wrappers you may have found, the correctness of this library is guaranteed by the reputation of this author. I have personally already noticed, and reported, bugs in other Folia wrappers based on API abuse.

### Access the command map

On Spigot, accessing the command map must be done with reflection. On Paper there is supported API: `server.getCommandMap()`. Relying on API, if it exists, makes your plugin more stable and impervious to future changes in the Paper implementation.

MorePaperLib's `getServerCommandMap` method automatically uses whichever approach is suitable to the platform.

### Adventure methods

Kick players with a `Component`. Disallow login events with a `Component` reason. If Adventure is not supported, fallback occurs automatically to legacy text serialization.

## Library Details

Supports Spigot and Paper from 1.8.8 to the latest version. Compatibility with Folia is guaranteed only for the latest Folia release, because Folia is unstable software.

### Artifact Info

The artifact is:

```xml
<dependency>
    <groupId>space.arim.morepaperlib</groupId>
    <artifactId>morepaperlib</artifactId>
    <version>0.4.3</version>
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
