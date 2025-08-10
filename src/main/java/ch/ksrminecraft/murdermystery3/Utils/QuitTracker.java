package ch.ksrminecraft.murdermystery3.Utils;

import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QuitTracker {
    private static final Set<UUID> quitDuringGame = new HashSet<>();



    public static void mark(Player p) {
        quitDuringGame.add(p.getUniqueId());
    }

    public static boolean hasQuit(Player p) {
        return quitDuringGame.contains(p.getUniqueId());
    }

    public static void clear(Player p) {
        quitDuringGame.remove(p.getUniqueId());
    }
}
