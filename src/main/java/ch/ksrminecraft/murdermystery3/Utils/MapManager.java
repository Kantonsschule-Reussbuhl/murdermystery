package ch.ksrminecraft.murdermystery3.Utils;

import ch.ksrminecraft.murdermystery3.MurderMystery3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MapManager {

    public static void teleportToRandomMap(Collection<UUID> playerUUIDs) {
        FileConfiguration cfg = MurderMystery3.getInstance().getConfig();
        List<String> maps = cfg.getStringList("worlds.maps");
        if (maps.isEmpty()) {
            Bukkit.getLogger().severe("Keine Maps im Config gefunden!");
            return;
        }

        String chosen = maps.get(new Random().nextInt(maps.size()));
        World map = Bukkit.getWorld(chosen);
        if (map == null) {
            Bukkit.getLogger().severe("Map '" + chosen + "' konnte nicht geladen werden!");
            return;
        }

        Location loc = map.getSpawnLocation();

        for (UUID uuid : playerUUIDs) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                p.teleport(loc);
            }
        }
    }


    public static void teleportToMainWorld(List<Player> players) {
        FileConfiguration cfg = MurderMystery3.getInstance().getConfig();
        String mainName = cfg.getString("worlds.main");
        if (mainName == null) return;
        World main = Bukkit.getWorld(mainName);
        if (main == null) return;
        Location loc = main.getSpawnLocation();
        players.forEach(p -> p.teleport(loc));
    }
}
