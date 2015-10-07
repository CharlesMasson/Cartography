package cartographie.positionnementGéographique;

/**
 * La classe {@code Coordonnées} hérite de la classe {@link Point} et réprésente un point repéré par sa latitude et sa longitude.
 *
 * @author Charles Masson
 */
public class Coordonnées extends Point {

    /**
     * la latitude du point
     */
    private Latitude latitude;
    /**
     * la longitude du point
     */
    private Longitude longitude;

    /**
     * Construit le point à partir de sa latitude et de sa longitude.
     *
     * @param latitude la latitude du point.
     * @param longitude la longitude du point.
     */
    public Coordonnées(Latitude latitude, Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Construit le point à partir de sa latitude et de sa longitude, exprimées en degrés, minutes et secondes.
     *
     * @param latitudeDegrés la part en degrés de la latitude.
     * @param latitudeMinutes la part en minutes de la latitude.
     * @param latitudeSecondes la part en secondes de la latitude.
     * @param longitudeDegrés la part en degrés de la longitude.
     * @param longitudeMinutes la part en minutes de la longitude.
     * @param longitudeSecondes la part en secondes de la longitude.
     */
    public Coordonnées(double latitudeDegrés, double latitudeMinutes, double latitudeSecondes, double longitudeDegrés, double longitudeMinutes,
            double longitudeSecondes) {
        latitude = new Latitude(latitudeDegrés, latitudeMinutes, latitudeSecondes);
        longitude = new Longitude(longitudeDegrés, longitudeMinutes, longitudeSecondes);
    }

    @Override
    public Latitude getLatitude() {
        return latitude;
    }

    @Override
    public Longitude getLongitude() {
        return longitude;
    }
}
