package ch.ksrminecraft.murdermystery3.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;

public class RoleManager {

    // Speichert dauerhaft die Rollen
    private static final Map<UUID, Role> roles = new HashMap<>();

    public static Map<UUID, Role> assignRoles(Set<UUID> players) {
        roles.clear();

        List<UUID> shuffled = new ArrayList<>(players);
        Collections.shuffle(shuffled);

        if (shuffled.size() < 3) return roles;

        UUID murderer = shuffled.remove(0);
        UUID detective = shuffled.remove(0);

        roles.put(murderer, Role.MURDERER);
        roles.put(detective, Role.DETECTIVE);

        for (UUID uuid : shuffled) {
            roles.put(uuid, Role.BYSTANDER);
        }

        return Collections.unmodifiableMap(roles);
    }

    public static Role getRole(UUID uuid) {
        return roles.get(uuid);
    }

    public static void setRole(UUID uuid, Role role) {
        roles.put(uuid, role);
    }

    public static Map<UUID, Role> getAllRoles() {
        return new HashMap<>(roles);
    }

    public static void removePlayer(UUID uuid) {
        roles.remove(uuid);
    }

    // Methode zum Holen des Detectives
    public static Player getDetective() {
        Optional<UUID> detectiveUuid = roles.entrySet().stream()
                .filter(e -> e.getValue() == Role.DETECTIVE)
                .map(Map.Entry::getKey)
                .findFirst();

        return detectiveUuid.map(Bukkit::getPlayer).orElse(null);
    }

    // Methode zum Holen aller Bystander (liefert UUIDs zurück)
    public static Set<UUID> getBystanders() {
        Set<UUID> bystanders = new HashSet<>();
        for (Map.Entry<UUID, Role> e : roles.entrySet()) {
            if (e.getValue() == Role.BYSTANDER) {
                bystanders.add(e.getKey());
            }
        }
        return bystanders;
    }
}
