package cartographie.positionnementGéographique;

import org.w3c.dom.Element;

/**
 * La clase {@code Longitude} représente une longitude.
 *
 * @author Charles
 */
public class Longitude extends Angle {

    /**
     * Construit une longitude à partir d'un angle en radians.
     *
     * @param valeurRadians l'angle en radians (0 représente le méridien de Greenwich).
     */
    public Longitude(double valeurRadians) {
        super(valeurRadians);
    }

    /**
     * Construit une longitude à partir d'un angle spécifié en degrés, minutes et secondes.
     *
     * @param valeurDegrés la part en degrés de la longitude.
     * @param valeurMinutes la part en minutes de la longitude.
     * @param valeurSecondes la part en secondes de la longitude.
     */
    public Longitude(double valeurDegrés, double valeurMinutes, double valeurSecondes) {
        super(valeurDegrés, valeurMinutes, valeurSecondes);
    }

    /**
     * Construit une longitude à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant une longitude.
     */
    public Longitude(Element e) {
        super(e);
    }

    /**
     * Renvoie la longitude minimale parmi deux longitudes.
     *
     * @param a1 une longitude.
     * @param a2 une longitude.
     * @return la longitude minimale.
     */
    public static Longitude min(Longitude a1, Longitude a2) {
        return new Longitude(Math.min(a1.getValeurRadians(), a2.getValeurRadians()));
    }

    /**
     * Renvoie la longitude maximale parmi deux longitudes.
     *
     * @param a1 une longitude.
     * @param a2 une longitude.
     * @return la longitude maximale.
     */
    public static Longitude max(Longitude a1, Longitude a2) {
        return new Longitude(Math.max(a1.getValeurRadians(), a2.getValeurRadians()));
    }

    @Override
    public String toString() {
        if (getValeurRadians() >= 0) {
            double valeurDegrés = getValeurDegrés();
            return (int) (valeurDegrés) + "° " + (int) ((60 * valeurDegrés) % 60) + "' " + (int) ((3600 * valeurDegrés) % 60) + "\" E";
        } else {
            double valeurDegrés = -getValeurDegrés();
            return (int) (valeurDegrés) + "° " + (int) ((60 * valeurDegrés) % 60) + "' " + (int) ((3600 * valeurDegrés) % 60) + "\" O";
        }
    }
}
