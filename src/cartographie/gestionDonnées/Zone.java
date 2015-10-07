package cartographie.gestionDonnées;

import cartographie.positionnementGéographique.Latitude;
import cartographie.positionnementGéographique.Longitude;
import cartographie.positionnementGéographique.Point;
import cartographie.positionnementGéographique.Terre;

/**
 * La classe {@code Zone} représente une zone du planisphère terrestre. Cette zone est en fait un rectangle défini par deux latitudes et deux longitudes
 * limites.
 *
 * @author Charles Masson
 */
public class Zone {

    /**
     * latitude limite
     */
    private Latitude latitudeMin, latitudeMax;
    /**
     * longitude limite
     */
    private Longitude longitudeMin, longitudeMax;

    /**
     * Construit une zone à partir des latitudes et longitudes qui la délimitent.
     *
     * @param latitudeMin latitude minimale de la zone.
     * @param latitudeMax latitude maximale de la zone.
     * @param longitudeMin longitude minimale de la zone.
     * @param longitudeMax longitude maximale de la zone.
     */
    public Zone(Latitude latitudeMin, Latitude latitudeMax, Longitude longitudeMin, Longitude longitudeMax) {
        this.latitudeMin = latitudeMin;
        this.latitudeMax = latitudeMax;
        this.longitudeMin = longitudeMin;
        this.longitudeMax = longitudeMax;
    }

    /**
     * Construit la plus petite zone contenant deux zones.
     *
     * @param z1 la première zone.
     * @param z2 la seconde zone.
     */
    public Zone(Zone z1, Zone z2) {
        this(Latitude.min(z1.latitudeMin, z2.latitudeMin),
                Latitude.max(z1.latitudeMax, z2.latitudeMax),
                Longitude.min(z1.longitudeMin, z2.longitudeMin),
                Longitude.max(z1.longitudeMax, z2.longitudeMax));
    }

    /**
     * Construit la plus petite zone contenant des points.
     *
     * @param points les points servant à la construction de la zone.
     */
    public Zone(Point... points) {
        latitudeMin = points[0].getLatitude();
        latitudeMax = latitudeMin;
        longitudeMin = points[0].getLongitude();
        longitudeMax = longitudeMin;
        for (int i = 1; i < points.length; i++) {
            latitudeMin = Latitude.min(latitudeMin, points[i].getLatitude());
            latitudeMax = Latitude.max(latitudeMax, points[i].getLatitude());
            longitudeMin = Longitude.min(longitudeMin, points[i].getLongitude());
            longitudeMax = Longitude.max(longitudeMax, points[i].getLongitude());
        }
    }

    /**
     * Construit la plus petite zone contenant un disque.
     *
     * @param centre le centre du disque.
     * @param rayonMétrique le rayon du disque en mètres.
     */
    public Zone(Point centre, double rayonMétrique) {
        double dLat = rayonMétrique / Terre.RAYON;
        double dLong = rayonMétrique / Terre.rayon(centre);
        this.latitudeMin = new Latitude(centre.getLatitude().getValeurRadians() - dLat);
        this.latitudeMax = new Latitude(centre.getLatitude().getValeurRadians() + dLat);
        this.longitudeMin = new Longitude(centre.getLongitude().getValeurRadians() - dLong);
        this.longitudeMax = new Longitude(centre.getLongitude().getValeurRadians() + dLong);
    }

    /**
     * Renvoie la lattitude minimale de cette zone.
     *
     * @return la latitude minimale.
     */
    public Latitude getLatitudeMin() {
        return latitudeMin;
    }

    /**
     * Renvoie la latitude maximale de cette zone.
     *
     * @return la latitude maximale.
     */
    public Latitude getLatitudeMax() {
        return latitudeMax;
    }

    /**
     * Renvoie la longitude minimale de cette zone.
     *
     * @return la longitude minimale.
     */
    public Longitude getLongitudeMin() {
        return longitudeMin;
    }

    /**
     * Renvoie la longitude maximale de cette zone.
     *
     * @return la longitude maximale.
     */
    public Longitude getLongitudeMax() {
        return longitudeMax;
    }

    @Override
    public String toString() {
        return "LatMin = " + latitudeMin + " | LatMax = " + latitudeMax + " | LongMin = " + longitudeMin + " | LongMax = " + longitudeMax;
    }
}
