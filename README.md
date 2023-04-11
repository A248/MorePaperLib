
# MorePaperLib

### Gracefully and simultaneously support Spigot, Paper, Folia, etc.

Need to support Folia? Spigot? Well, MorePaperLib has you covered. Now you can effortlessly support multiple versions of Spigot, Paper, **and** Folia.

MorePaperLib covers common use cases which may require different approaches depending on the target platform. It also provides shortcuts and optimizations for platforms supporting certain API.

## Features

### Folia scheduling

Paper's Folia project refactored the scheduling APIs. MorePaperLib provides a scheduling API.

Correct usage of the Folia API is critical to server success. Unlike other Folia wrappers you may have found, the correctness of this library is guaranteed by the reputation of this author. I have personally already noticed, and reported, bugs in other Folia wrappers based on API abuse.

### Access the command map

On Spigot, accessing the command map must be done with reflection. On Paper there is supported API: `server.getCommandMap()`. Relying on API, if it exists, makes your plugin more stable and impervious to future changes in the Paper implementation.

MorePaperLib's `getServerCommandMap` method automatically uses whichever approach is suitable to the platform.

### Adventure methods

Kick players with a `Component`. Disallow login events with a `Component` reason. If Adventure is not supported, fallback occurs automatically to legacy text serialization.

## Library Details

### Artifact Info

The artifact is:

```xml
<dependency>
    <groupId>space.arim.morepaperlib</groupId>
    <artifactId>morepaperlib</artifactId>
    <version>0.4.1</version>
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

Licensed under the GNU Lesser GPL v3, or later.
