package cartographie.positionnementGéographique;

import org.w3c.dom.Element;

/**
 * La classe {@code Latitude} représente une latitude.
 *
 * @author Charles
 */
public class Latitude extends Angle {

    /**
     * latitude minimale d'un point sur Terre (-<i>pi</i>/2, correspondant au pôle sud)
     */
    public static final Latitude LATITUDE_MIN = new Latitude(-Math.PI / 2);
    /**
     * latitude maximale d'un point sur Terre (<i>pi</i>/2, correspondant au pôle nord)
     */
    public static final Latitude LATITUDE_MAX = new Latitude(Math.PI / 2);

    /**
     * Construit une latitude à partir d'un angle en radians.
     *
     * @param valeurRadians l'angle en radians (0 représente l'équateur, <i>pi</i> représente le pôle nord et -<i>pi</i> représente le pôle sud).
     */
    public Latitude(double valeurRadians) {
        super(valeurRadians);
    }

    /**
     * Construit une latitude à partir d'un angle spécifié en degrés, minutes et secondes.
     *
     * @param valeurDegrés la part en degrés de la latitude.
     * @param valeurMinutes la part en minutes de la latitude.
     * @param valeurSecondes la part en secondes de la latitude.
     */
    public Latitude(double valeurDegrés, double valeurMinutes, double valeurSecondes) {
        super(valeurDegrés, valeurMinutes, valeurSecondes);
    }

    /**
     * Construit une latitude à partir d'un élément DOM (utile pour le décodage XML).
     *
     * @param e un élément DOM représentant une latitude.
     */
    public Latitude(Element e) {
        super(e);
    }

    /**
     * Renvoie la latitude minimale parmi deux latitudes.
     *
     * @param a1 une latitude.
     * @param a2 une latitude.
     * @return la latitude minimale.
     */
    public static Latitude min(Latitude a1, Latitude a2) {
        return new Latitude(Math.min(a1.getValeurRadians(), a2.getValeurRadians()));
    }

    /**
     * Renvoie la latitude maximale parmi deux latitudes.
     *
     * @param a1 une latitude.
     * @param a2 une latitude.
     * @return la latitude maximale.
     */
    public static Latitude max(Latitude a1, Latitude a2) {
        return new Latitude(Math.max(a1.getValeurRadians(), a2.getValeurRadians()));
    }

    @Override
    public String toString() {
        if (getValeurRadians() >= 0) {
            double valeurDegrés = getValeurDegrés();
            return (int) (valeurDegrés) + "° " + (int) ((60 * valeurDegrés) % 60) + "' " + (int) ((3600 * valeurDegrés) % 60) + "\" N";
        } else {
            double valeurDegrés = -getValeurDegrés();
            return (int) (valeurDegrés) + "° " + (int) ((60 * valeurDegrés) % 60) + "' " + (int) ((3600 * valeurDegrés) % 60) + "\" S";
        }
    }
}
