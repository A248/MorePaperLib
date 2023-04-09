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

package space.arim.morepaperlib.scheduling;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import space.arim.morepaperlib.MorePaperLib;

/**
 * Bridges scheduling APIs between Folia and Bukkit. Callers must specify where tasks are run:
 * whether the global region, a certain region, or on an entity scheduler, per Folia mechanics.
 * When running on Bukkit, automatic fallback occurs to the BukkitScheduler.
 *
 */
public class GracefulScheduling {

	private final MorePaperLib morePaperLib;
	private final FoliaDetection foliaDetection;

	/**
	 * Creates with the specified folia detection mechanism
	 *
	 * @param morePaperLib central command
	 * @param foliaDetection how to detect the presence of Folia APIs
	 */
	public GracefulScheduling(MorePaperLib morePaperLib, FoliaDetection foliaDetection) {
		this.morePaperLib = morePaperLib;
		this.foliaDetection = foliaDetection;
	}

	/**
	 * Creates using automatic folia detection
	 *
	 * @param morePaperLib central command
	 */
	public GracefulScheduling(MorePaperLib morePaperLib) {
		this(morePaperLib, StandardFoliaDetection.INSTANCE);
	}

	/**
	 * Determines whether Folia APIs are being used
	 *
	 * @return true if Folia APIs will be used
	 */
	public boolean isUsingFolia() {
		return foliaDetection.isUsingFolia();
	}

	private Server server() {
		return morePaperLib.getPlugin().getServer();
	}

	/**
	 * Obtains a scheduler which posts tasks to the global region on Folia, or the main thread on Bukkit
	 *
	 * @return a scheduler for the global region
	 */
	public RegionalScheduler globalRegionalScheduler() {
		if (isUsingFolia()) {
			return new GlobalScheduler(
					server().getGlobalRegionScheduler(), morePaperLib.getPlugin()
			);
		}
		return new BukkitSchedulerAsRegionalScheduler(morePaperLib);
	}

	/**
	 * Obtains a scheduler which posts tasks to a specific region on Folia. <b>Remember that it is inappropriate
	 * to use region specific scheduling for entities</b> <br>
	 * <br>
	 * On Bukkit, this scheduler simply posts to the main thread.
	 *
	 * @param location the location
	 * @return a scheduler for the specific region
	 */
	public RegionalScheduler regionSpecificScheduler(Location location) {
		return regionSpecificScheduler(location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
	}

	/**
	 * Obtains a scheduler which posts tasks to a specific region on Folia. <b>Remember that it is inappropriate
	 * to use region specific scheduling for entities</b> <br>
	 * <br>
	 * On Bukkit, this scheduler simply posts to the main thread.
	 *
	 * @param world the world
	 * @param chunkX the chunk X coordinate
	 * @param chunkZ the chunk Z coordinate
	 * @return a scheduler for the specific region
	 */
	public RegionalScheduler regionSpecificScheduler(World world, int chunkX, int chunkZ) {
		if (isUsingFolia()) {
			return new RegionSpecificScheduler(
					server().getRegionScheduler(), morePaperLib.getPlugin(), world, chunkX, chunkZ
			);
		}
		return new BukkitSchedulerAsRegionalScheduler(morePaperLib);
	}

	/**
	 * Obtains a scheduler which runs tasks for a certain entity. On Bukkit, this scheduler simply posts to
	 * the main thread
	 *
	 * @param entity the entity
	 * @return a scheduler for the entity
	 */
	public AttachedScheduler entitySpecificScheduler(Entity entity) {
		if (isUsingFolia()) {
			return new EntitySpecificScheduler(
					entity.getScheduler(), morePaperLib.getPlugin()
			);
		}
		return new BukkitSchedulerAsRegionalScheduler(morePaperLib).asAttachedScheduler();
	}

	/**
	 * Whether the current thread is the thread ticking the global region on Folia, or the main thread on Bukkit
	 *
	 * @return true if currently running on the global region thread, or the main thread on Bukkit
	 */
	public boolean isOnGlobalRegionThread() {
		if (isUsingFolia()) {
			return server().isGlobalTickThread();
		}
		return server().isPrimaryThread();
	}

}
