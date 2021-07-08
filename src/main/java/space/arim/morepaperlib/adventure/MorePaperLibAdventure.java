package space.arim.morepaperlib.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import space.arim.morepaperlib.MorePaperLib;

import java.util.Objects;

/**
 * Additional extensions which depend on the adventure API. <br>
 * <br>
 * All public methods are implemented independently of each other.
 *
 *
 */
public class MorePaperLibAdventure {
    
    private final MorePaperLib morePaperLib;

    public MorePaperLibAdventure(MorePaperLib morePaperLib) {
        this.morePaperLib = Objects.requireNonNull(morePaperLib, "morePaperLib");
    }

    /**
     * Gets the {@code MorePaperLib} associated with this {@code MorePaperLibAdventur}
     *
     * @return the morepaperlib object
     */
    public MorePaperLib getMorePaperLib() {
        return morePaperLib;
    }

    /*
    These implementations do not try-catch NoSuchMethodError because these methods
    may be more sensitive to performance than the ones in MorePaperLib
     */

    private static boolean methodExists(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
        try {
            clazz.getMethod(methodName, parameterTypes);
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    private static final boolean HAS_KICK_METHOD = methodExists(Player.class, "kick", Component.class);
    /**
     * Kicks a player with the given reason. If the {@code Player#kick(Component)}
     * method does not exist, legacy formatting is used.
     *
     * @param player the player to kick
     * @param message the kicm essage
     */
    public void kickPlayer(Player player, Component message) {
        if (HAS_KICK_METHOD) {
            player.kick(message);
        } else {
            player.kickPlayer(LegacyComponentSerializer.legacySection().serialize(message));
        }
    }

}
