package ch.ksrminecraft.murdermystery3.Utils;

import ch.ksrminecraft.RankPointsAPI.PointsAPI;
import ch.ksrminecraft.murdermystery3.MurderMystery3;
import java.util.UUID;
import java.util.logging.Logger;

public class PointsManager {
    private final PointsAPI api;
    private final Logger logger;
    private final MurderMystery3 plugin;



    // Konstruktor
    public PointsManager(Logger logger, MurderMystery3 plugin) {
        this.logger = logger;
        this.plugin = plugin;

        // Daten aus Config laden
        String url = plugin.getConfig().getString("Rank-Points-API-url");
        String user = plugin.getConfig().getString("Rank-Points-API-user");
        String pass = plugin.getConfig().getString("Rank-Points-API-password");

        if (url == null || user == null || pass == null) {
            logger.severe("Fehlende Konfigurationswerte für PointsAPI! Bitte config.yml prüfen.");
            throw new IllegalStateException("Config-Werte unvollständig");
        }

        // PointsAPI initialisieren
        this.api = new PointsAPI(url, user, pass, logger, true);
    }



    // Spieler auswählen und beliebige Anzahl Punkte hinzufügen
    public void addPointsToPlayer(UUID uuid, int points) {
        boolean success = api.addPoints(uuid, points);
        int newPoints = api.getPoints(uuid);

        if (success) {
            logger.info("Punkte erfolgreich hinzugefügt. Neuer Punktestand von " + uuid + ": " + newPoints);
        } else {
            logger.warning("Fehler beim Hinzufügen der Punkte für Spieler: " + uuid);
        }
    }
}
