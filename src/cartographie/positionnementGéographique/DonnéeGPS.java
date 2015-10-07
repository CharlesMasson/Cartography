package cartographie.positionnementGéographique;

/**
 * La classe {@code DonnéeGPS} représente une donnée GPS, c'est-à-dire une position géographique, ou plus précisément un objet héritant de la classe
 * {@link Coordonnées} affecté d'une orientation et d'une incertitude. Il s'agit en fait du type de donnée renvoyé par un capteur GPS.
 *
 * @author Charles Masson
 */
public class DonnéeGPS extends Coordonnées {

    /**
     * orientation actuelle par rapport au Nord
     */
    private Angle orientation;
    /**
     * incertitude sur la position en mètres
     */
    private double incertitude;

    /**
     * Construit une donnée GPS.
     *
     * @param latitude la latitude de la position.
     * @param longitude la longitude de la position.
     * @param orientation l'orientation.
     * @param incertitude l'incertitude sur la position.
     */
    public DonnéeGPS(Latitude latitude, Longitude longitude, Angle orientation, double incertitude) {
        super(latitude, longitude);
        this.orientation = orientation;
        this.incertitude = incertitude;
    }
}
