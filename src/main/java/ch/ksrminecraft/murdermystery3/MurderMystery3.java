package ch.ksrminecraft.murdermystery3;

import ch.ksrminecraft.murdermystery3.Commands.JoinCommand;
import ch.ksrminecraft.murdermystery3.Listener.GameListener;
import ch.ksrminecraft.murdermystery3.Utils.GameManager;
import ch.ksrminecraft.murdermystery3.Utils.PointsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

// TODO:
// Spectators können Spiel noch nicht vorzeitig verlassen
// Gold farm für bystander zum bogen erspielen
// eigene Maps bauen

public class MurderMystery3 extends JavaPlugin {
    // Plugin Instanz
    private static MurderMystery3 instance;
    private GameManager gameManager;
    private PointsManager pointsManager;


    private CommandMap commandMap;
    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Config-Werte einlesen und prüfen
        int minPlayers = getConfig().getInt("min-players", 3);
        int countdownTime = getConfig().getInt("countdown-seconds", 10);
        if (minPlayers < 3) {
            getLogger().severe("Config-Fehler: MIN_PLAYERS muss mindestens 3 sein. Default 3 wird angewendet.");
            minPlayers = 3;
        }

        // PointsManager mit Config-Daten initialisieren
        pointsManager = new PointsManager(getLogger(), this);

        // GameManager mit PointsManager initialisieren
        gameManager = new GameManager(pointsManager, this);

        // Konfigurationswerte im GameManager setzen
        gameManager.setMinPlayers(minPlayers);
        gameManager.setCountdownTime(countdownTime);

        // Listener registrieren - GameListener braucht jetzt GameManager
        getServer().getPluginManager().registerEvents(new GameListener(gameManager), this);

        // Commands registrieren - JoinCommand braucht GameManager
        //getCommand("join").setExecutor(new JoinCommand(gameManager));
        // Hole PaperPluginManager
        // Im onEnable() ohne Paper-spezifische Klassen:
        // CommandMap per Reflection holen
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            getLogger().severe("Failed to get CommandMap!");
            e.printStackTrace();
        }

        if (commandMap != null) {
            commandMap.register("murdermystery3", new JoinCommand(gameManager));
        }





        // RankPointsAPI Check
        if (getServer().getPluginManager().getPlugin("RankPointsAPI") == null) {
            getLogger().severe("RankPointsAPI Plugin nicht gefunden! Punkteverteilung nicht möglich.");
        }
    }


    // Plugin Instanz Getter
    public static MurderMystery3 getInstance() {
        return instance;
    }

    // Getter für GameManager, falls du ihn extern brauchst
    public GameManager getGameManager() {
        return gameManager;
    }

    // Getter für PointsManager, falls du ihn extern brauchst
    public PointsManager getPointsManager() {
        return pointsManager;
    }

    // Boolean für das Spiel zentral speichern
    private boolean murdererKilledByBow = false;

    public boolean isMurdererKilledByBow() {
        return murdererKilledByBow;
    }

    public void setMurdererKilledByBow(boolean murdererKilledByBow) {
        this.murdererKilledByBow = murdererKilledByBow;
    }
}

